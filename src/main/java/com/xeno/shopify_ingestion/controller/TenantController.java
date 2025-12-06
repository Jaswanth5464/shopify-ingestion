package com.xeno.shopify_ingestion.controller;

import com.xeno.shopify_ingestion.model.Tenant;
import com.xeno.shopify_ingestion.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Value("${shopify.store.url}")
    private String storeUrl;

    @Value("${shopify.access.token}")
    private String accessToken;

    // Create tenant from properties
    @PostMapping("/create")
    public Tenant createTenant(@RequestParam String tenantName) {
        return tenantService.createOrGetTenant(tenantName, storeUrl, accessToken);
    }

    // Get tenant by ID
    @GetMapping("/{id}")
    public Tenant getTenant(@PathVariable Long id) {
        return tenantService.getTenantById(id);
    }
}
