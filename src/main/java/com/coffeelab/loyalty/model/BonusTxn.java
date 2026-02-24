package com.coffeelab.loyalty.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bonus_txn")
public class BonusTxn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BonusTxnType txnType;

    @Column(nullable = false)
    private BigDecimal signedAmount;

    private String reason;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // Constructors
    public BonusTxn() {}

    public BonusTxn(Client client, Order order, BonusTxnType txnType, BigDecimal signedAmount, String reason) {
        this.client = client;
        this.order = order;
        this.txnType = txnType;
        this.signedAmount = signedAmount;
        this.reason = reason;
        this.createdAt = Instant.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public BonusTxnType getTxnType() { return txnType; }
    public void setTxnType(BonusTxnType txnType) { this.txnType = txnType; }

    public BigDecimal getSignedAmount() { return signedAmount; }
    public void setSignedAmount(BigDecimal signedAmount) { this.signedAmount = signedAmount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}