package com.xeno.shopify_ingestion.controller;

import com.xeno.shopify_ingestion.model.Customer;
import com.xeno.shopify_ingestion.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    // GET all customers for a tenant
    @GetMapping("/{tenantId}")
    public List<Customer> getCustomersByTenant(@PathVariable Long tenantId) {
        return customerRepository.findByTenantId(tenantId);
    }
}
