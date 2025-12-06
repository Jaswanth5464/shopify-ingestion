package com.xeno.shopify_ingestion.model;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId;  // ⭐ This links to which store!

    private String shopifyCustomerId;  // Shopify's ID for this customer

    private String email;
    private String firstName;
    private String lastName;
    private Double totalSpent = 0.0;  // How much they spent
    private Integer ordersCount = 0;   // How many orders

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
