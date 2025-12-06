package com.xeno.shopify_ingestion.repository;



import com.xeno.shopify_ingestion.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTenantId(Long tenantId);

    Product findByTenantIdAndShopifyProductId(Long tenantId, String shopifyProductId);
}