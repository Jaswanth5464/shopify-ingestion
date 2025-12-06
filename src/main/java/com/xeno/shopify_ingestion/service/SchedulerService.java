package com.xeno.shopify_ingestion.service;

import com.xeno.shopify_ingestion.model.Tenant;
import com.xeno.shopify_ingestion.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private DataIngestionService dataIngestionService;

    // Run every 1 hour (3600000 milliseconds)
    // For testing: use 300000 (5 minutes)
    @Scheduled(fixedRate = 1000000)
    public void syncAllTenants() {
        System.out.println("=== AUTO SYNC STARTED at " + LocalDateTime.now() + " ===");

        // Get all active tenants
        List<Tenant> tenants = tenantRepository.findAll();

        if (tenants.isEmpty()) {
            System.out.println("No tenants found to sync");
            return;
        }

        int successCount = 0;
        int errorCount = 0;

        // Loop through each tenant and sync
        for (Tenant tenant : tenants) {
            if (!"ACTIVE".equals(tenant.getStatus())) {
                System.out.println("Skipping inactive tenant: " + tenant.getTenantName());
                continue;
            }

            try {
                System.out.println("Syncing tenant: " + tenant.getTenantName() + " (ID: " + tenant.getId() + ")");

                // Sync customers
                String customerResult = dataIngestionService.syncCustomers(tenant.getId());
                System.out.println("  Customers: " + customerResult);

                // Sync orders
                String orderResult = dataIngestionService.syncOrders(tenant.getId());
                System.out.println("  Orders: " + orderResult);

                // Sync products
                String productResult = dataIngestionService.syncProducts(tenant.getId());
                System.out.println("  Products: " + productResult);

                successCount++;

            } catch (Exception e) {
                System.err.println("Error syncing tenant " + tenant.getTenantName() + ": " + e.getMessage());
                errorCount++;
            }
        }

        System.out.println("=== AUTO SYNC COMPLETED ===");
        System.out.println("Success: " + successCount + " tenants");
        System.out.println("Errors: " + errorCount + " tenants");
        System.out.println("================================\n");
    }

    // Manual trigger endpoint (for testing)
    public String triggerManualSync() {
        syncAllTenants();
        return "Manual sync triggered successfully!";
    }
}
