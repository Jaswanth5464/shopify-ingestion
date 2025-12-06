package com.xeno.shopify_ingestion.repository;



import com.xeno.shopify_ingestion.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Tenant findByTenantName(String tenantName);
}