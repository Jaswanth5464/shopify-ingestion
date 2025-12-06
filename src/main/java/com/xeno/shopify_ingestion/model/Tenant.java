package com.xeno.shopify_ingestion.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Entity
@Table(name = "tenants")
@Data
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tenantName;

    @Column(nullable = false)
    private String shopifyStoreUrl;

    @Column(nullable = false)
    private String shopifyAccessToken;

    private String status = "ACTIVE";

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


}
