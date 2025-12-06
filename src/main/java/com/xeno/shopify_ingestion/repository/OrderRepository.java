package com.xeno.shopify_ingestion.repository;



import com.xeno.shopify_ingestion.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByTenantId(Long tenantId);

    Order findByTenantIdAndShopifyOrderId(Long tenantId, String shopifyOrderId);

    // Orders between dates
    List<Order> findByTenantIdAndOrderDateBetween(Long tenantId, LocalDateTime start, LocalDateTime end);

    // Count orders for a tenant
    @Query("SELECT COUNT(o) FROM Order o WHERE o.tenantId = ?1")
    Long countByTenantId(Long tenantId);

    // Total revenue
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.tenantId = ?1")
    Double sumTotalPriceByTenantId(Long tenantId);
}