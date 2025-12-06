package com.xeno.shopify_ingestion.model;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId;  // ⭐ Which store

    private String shopifyOrderId;
    private Long customerId;  // Links to our Customer table

    private String orderNumber;  // "#1001"
    private Double totalPrice;
    private String currency = "INR";
    private String status = "PENDING";  // PENDING/FULFILLED/CANCELLED

    private LocalDateTime orderDate;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}