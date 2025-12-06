package com.xeno.shopify_ingestion.service;

import com.xeno.shopify_ingestion.model.Customer;
import com.xeno.shopify_ingestion.model.Order;
import com.xeno.shopify_ingestion.repository.CustomerRepository;
import com.xeno.shopify_ingestion.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalyticsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 1. Get summary stats
   
    public Map<String, Object> getSummary(Long tenantId) {
        Map<String, Object> summary = new HashMap<>();

        // Total customers
        long totalCustomers = customerRepository.findByTenantId(tenantId).size();

        // Total orders
        long totalOrders = orderRepository.countByTenantId(tenantId);

        // Total revenue
        Double totalRevenue = orderRepository.sumTotalPriceByTenantId(tenantId);
        if (totalRevenue == null) totalRevenue = 0.0;

        // Average order value
        double avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;

        summary.put("totalCustomers", totalCustomers);
        summary.put("totalOrders", totalOrders);
        summary.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        summary.put("avgOrderValue", Math.round(avgOrderValue * 100.0) / 100.0);

        return summary;
    }

    // 2. Get top 5 customers by spend
   
    public List<Map<String, Object>> getTopCustomers(Long tenantId, int limit) {
        List<Customer> customers = customerRepository.findTop5ByTenantIdOrderByTotalSpentDesc(tenantId);

        List<Map<String, Object>> result = new ArrayList<>();

        int count = 0;
        for (Customer customer : customers) {
            if (count >= limit) break;

            Map<String, Object> customerData = new HashMap<>();
            customerData.put("name", customer.getFirstName() + " " + customer.getLastName());
            customerData.put("email", customer.getEmail());
            customerData.put("totalSpent", customer.getTotalSpent());
            customerData.put("ordersCount", customer.getOrdersCount());

            result.add(customerData);
            count++;
        }

        return result;
    }

    // 3. Get orders by date (for line chart)
   
    public List<Map<String, Object>> getOrdersByDate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders;

        if (startDate != null && endDate != null) {
            orders = orderRepository.findByTenantIdAndOrderDateBetween(tenantId, startDate, endDate);
        } else {
            orders = orderRepository.findByTenantId(tenantId);
        }

        // Group by date
        Map<String, Double> dateMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        for (Order order : orders) {
            if (order.getOrderDate() == null) continue;

            String date = order.getOrderDate().toLocalDate().toString();

            dateMap.put(date, dateMap.getOrDefault(date, 0.0) + order.getTotalPrice());
            countMap.put(date, countMap.getOrDefault(date, 0) + 1);
        }

        // Convert to list
        List<Map<String, Object>> result = new ArrayList<>();
        for (String date : dateMap.keySet()) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", date);
            dayData.put("revenue", Math.round(dateMap.get(date) * 100.0) / 100.0);
            dayData.put("orderCount", countMap.get(date));
            result.add(dayData);
        }

        // Sort by date
        result.sort((a, b) -> ((String) a.get("date")).compareTo((String) b.get("date")));

        return result;
    }

    // 4. Get order status distribution (for pie chart)
   
    public List<Map<String, Object>> getOrderStatusDistribution(Long tenantId) {
        List<Order> orders = orderRepository.findByTenantId(tenantId);

        Map<String, Integer> statusCount = new HashMap<>();

        for (Order order : orders) {
            String status = order.getStatus() != null ? order.getStatus() : "UNKNOWN";
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (String status : statusCount.keySet()) {
            Map<String, Object> statusData = new HashMap<>();
            statusData.put("status", status);
            statusData.put("count", statusCount.get(status));
            result.add(statusData);
        }

        return result;
    }

 
  

}
