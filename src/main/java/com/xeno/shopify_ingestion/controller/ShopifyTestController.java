package com.xeno.shopify_ingestion.controller;

import com.xeno.shopify_ingestion.service.ShopifyApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/shopify-test")
public class ShopifyTestController {

    @Autowired
    private ShopifyApiService shopifyApiService;

    @Value("${shopify.store.url}")
    private String storeUrl;

    @Value("${shopify.access.token}")
    private String accessToken;

    @GetMapping("/customers")
    public Map<String, Object> testFetchCustomers() {
        Map<String, Object> response = shopifyApiService.fetchCustomers(storeUrl, accessToken);

        // Print to console to debug
        System.out.println("=== SHOPIFY RESPONSE ===");
        System.out.println(response);

        return response;
    }
}