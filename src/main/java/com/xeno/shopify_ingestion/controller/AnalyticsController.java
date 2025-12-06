package com.xeno.shopify_ingestion.controller;

import com.xeno.shopify_ingestion.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    // Get summary stats
    @GetMapping("/summary/{tenantId}")
    public Map<String, Object> getSummary(@PathVariable Long tenantId) {
        return analyticsService.getSummary(tenantId);
    }

    // Get top customers
    @GetMapping("/top-customers/{tenantId}")
    public List<Map<String, Object>> getTopCustomers(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return analyticsService.getTopCustomers(tenantId, limit);
    }

    // Get orders by date (with optional date range)
    @GetMapping("/orders-by-date/{tenantId}")
    public List<Map<String, Object>> getOrdersByDate(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return analyticsService.getOrdersByDate(tenantId, startDate, endDate);
    }

    // Get order status distribution
    @GetMapping("/order-status/{tenantId}")
    public List<Map<String, Object>> getOrderStatus(@PathVariable Long tenantId) {
        return analyticsService.getOrderStatusDistribution(tenantId);
    }
}
