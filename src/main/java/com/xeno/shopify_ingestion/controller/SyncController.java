package com.xeno.shopify_ingestion.controller;

import com.xeno.shopify_ingestion.service.DataIngestionService;
import com.xeno.shopify_ingestion.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

    @Autowired
    private DataIngestionService dataIngestionService;
    @Autowired
    private SchedulerService schedulerService;
    // Sync customers for a tenant
    @PostMapping("/customers/{tenantId}")
    public String syncCustomers(@PathVariable Long tenantId) {
        return dataIngestionService.syncCustomers(tenantId);
    }

    // Sync orders
    @PostMapping("/orders/{tenantId}")
    public String syncOrders(@PathVariable Long tenantId) {
        return dataIngestionService.syncOrders(tenantId);
    }

    // Sync products
    @PostMapping("/products/{tenantId}")
    public String syncProducts(@PathVariable Long tenantId) {
        return dataIngestionService.syncProducts(tenantId);
    }

    // Sync everything at once
    @PostMapping("/all/{tenantId}")
    public String syncAll(@PathVariable Long tenantId) {
        String customers = dataIngestionService.syncCustomers(tenantId);
        String orders = dataIngestionService.syncOrders(tenantId);
        String products = dataIngestionService.syncProducts(tenantId);

        return customers + "\n" + orders + "\n" + products;
    }

    @PostMapping("/trigger-auto-sync")
    public String triggerAutoSync() {
        schedulerService.triggerManualSync();
        return "Auto-sync triggered manually!";
    }

}
