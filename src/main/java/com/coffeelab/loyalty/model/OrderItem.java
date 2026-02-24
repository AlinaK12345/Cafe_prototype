package com.coffeelab.loyalty.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String productNameSnap;

    @Column(nullable = false)
    private BigDecimal unitPriceSnap;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private BigDecimal lineTotal;

    // Constructors
    public OrderItem() {}

    public OrderItem(Order order, Product product, Integer qty) {
        this.order = order;
        this.product = product;
        this.productNameSnap = product.getName();
        this.unitPriceSnap = product.getPrice();
        this.qty = qty;
        this.lineTotal = product.getPrice().multiply(BigDecimal.valueOf(qty));
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getProductNameSnap() { return productNameSnap; }
    public void setProductNameSnap(String productNameSnap) { this.productNameSnap = productNameSnap; }

    public BigDecimal getUnitPriceSnap() { return unitPriceSnap; }
    public void setUnitPriceSnap(BigDecimal unitPriceSnap) { this.unitPriceSnap = unitPriceSnap; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}