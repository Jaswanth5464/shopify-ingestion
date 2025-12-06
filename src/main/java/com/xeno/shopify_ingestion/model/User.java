package com.xeno.shopify_ingestion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;  // Will be hashed

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Long tenantId;  // Link to tenant

    private String role = "USER";  // USER or ADMIN

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}