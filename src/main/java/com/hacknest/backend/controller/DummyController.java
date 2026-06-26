package com.hacknest.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dummy")
public class DummyController {

    @GetMapping("/hello")
    public Map<String, String> sayHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello! This is a dummy GET response.");
        response.put("status", "success");
        return response;
    }

    @GetMapping("/data")
    public Map<String, Object> getDummyData() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("name", "Test User");
        data.put("role", "Admin");
        data.put("active", true);
        return data;
    }

    @PostMapping("/submit")
    public Map<String, String> submitDummyData() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Dummy POST request received successfully.");
        return response;
    }
}
