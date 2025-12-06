package com.xeno.shopify_ingestion.service;

import com.xeno.shopify_ingestion.model.Tenant;
import com.xeno.shopify_ingestion.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    // Create or get existing tenant
    public Tenant createOrGetTenant(String tenantName, String storeUrl, String accessToken) {

        // Check if tenant already exists
        Tenant existing = tenantRepository.findByTenantName(tenantName);

        if (existing != null) {
            return existing;  // Already exists, return it
        }

        // Create new tenant
        Tenant tenant = new Tenant();
        tenant.setTenantName(tenantName);
        tenant.setShopifyStoreUrl(storeUrl);
        tenant.setShopifyAccessToken(accessToken);
        tenant.setStatus("ACTIVE");

        return tenantRepository.save(tenant);  // Save and return
    }

    // Get tenant by ID
    public Tenant getTenantById(Long tenantId) {
        return tenantRepository.findById(tenantId).orElse(null);
    }
}
