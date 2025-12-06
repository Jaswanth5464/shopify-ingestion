package com.xeno.shopify_ingestion.repository;



import com.xeno.shopify_ingestion.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByTenantId(Long tenantId);

    Customer findByTenantIdAndShopifyCustomerId(Long tenantId, String shopifyCustomerId);

    // Top 5 customers by spend
    @Query("SELECT c FROM Customer c WHERE c.tenantId = ?1 ORDER BY c.totalSpent DESC")
    List<Customer> findTop5ByTenantIdOrderByTotalSpentDesc(Long tenantId);

   // List<Customer> findByTenantId(Long tenantId);

}