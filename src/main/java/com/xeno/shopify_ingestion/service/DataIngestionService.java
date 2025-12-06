package com.xeno.shopify_ingestion.service;

import com.xeno.shopify_ingestion.model.Customer;
import com.xeno.shopify_ingestion.model.Tenant;
import com.xeno.shopify_ingestion.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xeno.shopify_ingestion.model.Order;
import com.xeno.shopify_ingestion.model.Product;
import com.xeno.shopify_ingestion.repository.OrderRepository;
import com.xeno.shopify_ingestion.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DataIngestionService {

  
    @Autowired
    private ShopifyApiService shopifyApiService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;


    // -------------------------------------------------------------------
    // ✅ FINAL UPDATED CUSTOMER SYNC METHOD
    // -------------------------------------------------------------------
    public String syncCustomers(Long tenantId) {

        Tenant tenant = tenantService.getTenantById(tenantId);
        if (tenant == null) {
            return "Tenant not found!";
        }

        Map<String, Object> response = shopifyApiService.fetchCustomers(
                tenant.getShopifyStoreUrl(),
                tenant.getShopifyAccessToken()
        );
        if (response == null) {
            return "❌ Failed to connect to Shopify. Check credentials.";
        }
        List<Map<String, Object>> shopifyCustomers =
                (List<Map<String, Object>>) response.get("customers");

        if (shopifyCustomers == null || shopifyCustomers.isEmpty()) {
            return "No customers found in Shopify";
        }

        int savedCount = 0;

        for (Map<String, Object> shopifyCustomer : shopifyCustomers) {

            String shopifyCustomerId = shopifyCustomer.get("id").toString();

            Customer existingCustomer =
                    customerRepository.findByTenantIdAndShopifyCustomerId(
                            tenantId,
                            shopifyCustomerId
                    );

            Customer customer = (existingCustomer != null)
                    ? existingCustomer
                    : new Customer();

            customer.setTenantId(tenantId);
            customer.setShopifyCustomerId(shopifyCustomerId);

            // SAFE FIELDS WITH DEFAULT VALUES
            customer.setEmail(getValue(shopifyCustomer, "email", "no-email@unknown.com"));
            customer.setFirstName(getValue(shopifyCustomer, "first_name", "Guest"));
            customer.setLastName(getValue(shopifyCustomer, "last_name", "Customer"));

            // total_spent
            Object totalSpent = shopifyCustomer.get("total_spent");
            customer.setTotalSpent(
                    totalSpent != null ? Double.parseDouble(totalSpent.toString()) : 0.0
            );

            // orders_count
            Object ordersCount = shopifyCustomer.get("orders_count");
            customer.setOrdersCount(
                    ordersCount != null ? Integer.parseInt(ordersCount.toString()) : 0
            );

            customerRepository.save(customer);
            savedCount++;
        }
     
        return "Synced " + savedCount + " customers successfully!";
    }



    // -------------------------------------------------------------------
    // Helper function: safe getter with default values
    // -------------------------------------------------------------------
    private String getValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            return defaultValue;
        }
        return value.toString();
    }



    // -------------------------------------------------------------------
    // ORDER SYNC (unchanged, working fine)
    // -------------------------------------------------------------------
    public String syncOrders(Long tenantId) {
        Tenant tenant = tenantService.getTenantById(tenantId);
        if (tenant == null) {
            return "Tenant not found!";
        }

        Map<String, Object> response = shopifyApiService.fetchOrders(
                tenant.getShopifyStoreUrl(),
                tenant.getShopifyAccessToken()
        );
        if (response == null) {
            return "❌ Failed to connect to Shopify. Check credentials.";
        }

        List<Map<String, Object>> shopifyOrders =
                (List<Map<String, Object>>) response.get("orders");

        if (shopifyOrders == null || shopifyOrders.isEmpty()) {
            return "No orders found in Shopify";
        }

        int savedCount = 0;

        for (Map<String, Object> shopifyOrder : shopifyOrders) {

            String shopifyOrderId = shopifyOrder.get("id").toString();

            Order existingOrder = orderRepository.findByTenantIdAndShopifyOrderId(
                    tenantId,
                    shopifyOrderId
            );

            Order order = (existingOrder != null)
                    ? existingOrder
                    : new Order();

            order.setTenantId(tenantId);
            order.setShopifyOrderId(shopifyOrderId);

            order.setOrderNumber((String) shopifyOrder.get("name"));

            Object totalPrice = shopifyOrder.get("total_price");
            if (totalPrice != null) {
                order.setTotalPrice(Double.parseDouble(totalPrice.toString()));
            }

            order.setCurrency((String) shopifyOrder.get("currency"));
            order.setStatus((String) shopifyOrder.get("financial_status"));

            String createdAt = (String) shopifyOrder.get("created_at");
            if (createdAt != null) {
                order.setOrderDate(LocalDateTime.parse(createdAt.substring(0, 19)));
            }

            orderRepository.save(order);
            savedCount++;
        }
        //analyticsService.clearCache();
        return "Synced " + savedCount + " orders successfully!";
    }



    // -------------------------------------------------------------------
    // PRODUCT SYNC (unchanged, working fine)
    // -------------------------------------------------------------------
    public String syncProducts(Long tenantId) {
        Tenant tenant = tenantService.getTenantById(tenantId);
        if (tenant == null) {
            return "Tenant not found!";
        }

        Map<String, Object> response = shopifyApiService.fetchProducts(
                tenant.getShopifyStoreUrl(),
                tenant.getShopifyAccessToken()
        );
        if (response == null) {
            return "❌ Failed to connect to Shopify. Check credentials.";
        }
        List<Map<String, Object>> shopifyProducts =
                (List<Map<String, Object>>) response.get("products");

        if (shopifyProducts == null || shopifyProducts.isEmpty()) {
            return "No products found in Shopify";
        }

        int savedCount = 0;

        for (Map<String, Object> shopifyProduct : shopifyProducts) {

            String shopifyProductId = shopifyProduct.get("id").toString();

            Product existingProduct = productRepository.findByTenantIdAndShopifyProductId(
                    tenantId,
                    shopifyProductId
            );

            Product product = (existingProduct != null)
                    ? existingProduct
                    : new Product();

            product.setTenantId(tenantId);
            product.setShopifyProductId(shopifyProductId);

            product.setTitle((String) shopifyProduct.get("title"));

            List<Map<String, Object>> variants =
                    (List<Map<String, Object>>) shopifyProduct.get("variants");

            if (variants != null && !variants.isEmpty()) {
                Object price = variants.get(0).get("price");
                if (price != null) {
                    product.setPrice(Double.parseDouble(price.toString()));
                }

                Object inventory = variants.get(0).get("inventory_quantity");
                if (inventory != null) {
                    product.setInventoryQuantity(Integer.parseInt(inventory.toString()));
                }
            }

            productRepository.save(product);
            savedCount++;
        }
       // analyticsService.clearCache();
        return "Synced " + savedCount + " products successfully!";
    }
}
