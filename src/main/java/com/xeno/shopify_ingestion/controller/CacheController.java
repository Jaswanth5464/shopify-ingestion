package com.xeno.shopify_ingestion.controller;

import com.xeno.shopify_ingestion.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private AnalyticsService analyticsService;

    // Check cache status
    @GetMapping("/info")
    public Map<String, Object> getCacheInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("cacheType", "Redis");
        info.put("caches", cacheManager.getCacheNames());
        info.put("ttl", "1 hour");

        return info;
    }

    // Clear all caches manually
    @PostMapping("/clear")
    public String clearCache() {
        analyticsService.clearCache();
        return "✅ All caches cleared!";
    }
}