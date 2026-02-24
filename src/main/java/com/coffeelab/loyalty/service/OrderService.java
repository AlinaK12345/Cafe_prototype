package com.coffeelab.loyalty.service;

import com.coffeelab.loyalty.dto.OrderDto;
import com.coffeelab.loyalty.model.*;
import com.coffeelab.loyalty.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final BonusTxnRepository bonusTxnRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository,
                        ClientRepository clientRepository,
                        UserRepository userRepository,
                        BonusTxnRepository bonusTxnRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.bonusTxnRepository = bonusTxnRepository;
    }

    @Transactional
    public OrderDto createOrder(Long createdByUserId, String phone) {
        User user = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + createdByUserId));

        Client client = null;
        if (phone != null && !phone.isBlank()) {
            client = clientRepository.findByPhone(phone).orElse(null);
        }

        Order order = new Order();
        order.setCreatedBy(user);
        order.setClient(client);
        order.setStatus(OrderStatus.NEW);
        order.setSubtotal(BigDecimal.ZERO);
        order.setBonusSpent(BigDecimal.ZERO);
        order.setTotal(BigDecimal.ZERO);

        order = orderRepository.save(order);
        return toDto(order);
    }

    @Transactional
    public OrderDto addItem(Long orderId, Long productId, Integer qty) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("Cannot modify non-NEW order");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (!product.isActive()) {
            throw new IllegalStateException("Product is not active");
        }

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setProductNameSnap(product.getName());
        item.setUnitPriceSnap(product.getPrice());
        item.setQty(qty);
        item.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(qty))
                .setScale(2, RoundingMode.HALF_UP));
        orderItemRepository.save(item);

        recalcTotals(order);
        order = orderRepository.save(order);
        return toDto(order);
    }

    @Transactional
    public OrderDto spendBonus(Long orderId, BigDecimal requestedAmount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("Cannot spend bonus for non-NEW order");
        }

        if (order.getClient() == null) {
            throw new IllegalStateException("Order has no client assigned");
        }

        if (bonusTxnRepository.existsByOrder_IdAndTxnType(orderId, BonusTxnType.SPEND)) {
            throw new IllegalStateException("Bonuses already spent for this order");
        }

        Client client = clientRepository.findById(order.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        BigDecimal subtotal = order.getSubtotal();
        BigDecimal limit30 = subtotal.multiply(new BigDecimal("0.30"))
                .setScale(2, RoundingMode.HALF_UP);

        // Calculate allowed spend amount
        BigDecimal toSpend = requestedAmount;
        if (toSpend.compareTo(limit30) > 0) {
            toSpend = limit30;
        }
        if (toSpend.compareTo(client.getBonusBalance()) > 0) {
            toSpend = client.getBonusBalance();
        }
        toSpend = toSpend.setScale(2, RoundingMode.HALF_UP);

        if (toSpend.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Spend amount must be > 0 and within limits");
        }

        // Create SPEND transaction
        BonusTxn bonusTxn = new BonusTxn();
        bonusTxn.setClient(client);
        bonusTxn.setOrder(order);
        bonusTxn.setTxnType(BonusTxnType.SPEND);
        bonusTxn.setSignedAmount(toSpend.negate());
        bonusTxn.setReason("Spend bonuses (1 bonus = 1 rub)");
        bonusTxnRepository.save(bonusTxn);

        // Update balances
        client.setBonusBalance(client.getBonusBalance().subtract(toSpend));
        clientRepository.save(client);

        order.setBonusSpent(toSpend);
        order.setTotal(subtotal.subtract(toSpend)
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP));
        order = orderRepository.save(order);

        return toDto(order);
    }

    @Transactional
    public OrderDto completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("Only NEW order can be completed");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(Instant.now());
        order = orderRepository.save(order);

        // Give cashback if client exists
        if (order.getClient() != null) {
            Client client = clientRepository.findById(order.getClient().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));

            BigDecimal cashback = order.getTotal()
                    .multiply(new BigDecimal("0.05"))
                    .setScale(2, RoundingMode.HALF_UP);

            if (cashback.compareTo(BigDecimal.ZERO) > 0) {
                BonusTxn bonusTxn = new BonusTxn();
                bonusTxn.setClient(client);
                bonusTxn.setOrder(order);
                bonusTxn.setTxnType(BonusTxnType.EARN);
                bonusTxn.setSignedAmount(cashback);
                bonusTxn.setReason("Cashback 5% after completion");
                bonusTxnRepository.save(bonusTxn);

                client.setBonusBalance(client.getBonusBalance().add(cashback));
                clientRepository.save(client);
            }
        }

        return toDto(order);
    }

    private void recalcTotals(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrder_Id(order.getId());
        BigDecimal subtotal = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        order.setSubtotal(subtotal);
        order.setTotal(subtotal.subtract(order.getBonusSpent())
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP));
    }

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.id = order.getId();
        dto.clientId = order.getClient() != null ? order.getClient().getId() : null;
        dto.status = order.getStatus();
        dto.subtotal = order.getSubtotal();
        dto.bonusSpent = order.getBonusSpent();
        dto.total = order.getTotal();
        return dto;
    }
}