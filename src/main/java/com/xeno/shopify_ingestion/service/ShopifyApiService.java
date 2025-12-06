package com.xeno.shopify_ingestion.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@Service
public class ShopifyApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Fetch customers from Shopify
    public Map<String, Object> fetchCustomers(String storeUrl, String accessToken) {
        String url = String.format("https://%s/admin/api/2024-01/customers.json?fields=id,email,first_name,last_name,total_spent,orders_count,created_at", storeUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();
    }

    // Fetch orders from Shopify
    public Map<String, Object> fetchOrders(String storeUrl, String accessToken) {
        String url = String.format("https://%s/admin/api/2024-01/orders.json?status=any", storeUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();
    }

    // Fetch products from Shopify
    public Map<String, Object> fetchProducts(String storeUrl, String accessToken) {
        String url = String.format("https://%s/admin/api/2024-01/products.json", storeUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();
    }
}