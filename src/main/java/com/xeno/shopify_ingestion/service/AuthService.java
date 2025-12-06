package com.xeno.shopify_ingestion.service;

import com.xeno.shopify_ingestion.model.Tenant;
import com.xeno.shopify_ingestion.model.User;
import com.xeno.shopify_ingestion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantService tenantService;

    // Signup
    public Map<String, Object> signup(String email, String password, String fullName, String storeName, String storeUrl, String accessToken) {
        Map<String, Object> response = new HashMap<>();

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            response.put("success", false);
            response.put("message", "Email already registered");
            return response;
        }

        // Create tenant first
        Tenant tenant = tenantService.createOrGetTenant(storeName, storeUrl, accessToken);

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashPassword(password));  // Simple hash (use BCrypt in production)
        user.setFullName(fullName);
        user.setTenantId(tenant.getId());

        userRepository.save(user);

        response.put("success", true);
        response.put("message", "Account created successfully");
        response.put("userId", user.getId());
        response.put("tenantId", tenant.getId());

        return response;
    }

    // Login
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            response.put("success", false);
            response.put("message", "Email not found");
            return response;
        }

        // Check password
        if (!user.getPassword().equals(hashPassword(password))) {
            response.put("success", false);
            response.put("message", "Incorrect password");
            return response;
        }

        // Login successful
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("userId", user.getId());
        response.put("tenantId", user.getTenantId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());

        return response;
    }

    // Simple password hashing (use BCrypt in production!)
    private String hashPassword(String password) {
        // For demo: just add "HASHED_" prefix
        // In production: use BCryptPasswordEncoder
        return "HASHED_" + password;
    }
}