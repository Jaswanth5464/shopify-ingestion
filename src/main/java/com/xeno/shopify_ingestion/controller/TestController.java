package com.xeno.shopify_ingestion.controller;

import com.xeno.shopify_ingestion.model.TestEntity;
import com.xeno.shopify_ingestion.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestRepository testRepository;


    @PostMapping("/create")
    public TestEntity create(@RequestParam String message) {
        TestEntity t = new TestEntity();
        t.setMessage(message);
        return testRepository.save(t);
    }

    @GetMapping("/all")
    public List<TestEntity> getAll() {
        return testRepository.findAll();
    }
}
