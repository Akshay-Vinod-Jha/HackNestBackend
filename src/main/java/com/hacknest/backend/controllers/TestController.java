package com.hacknest.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

@RestController
@RequestMapping("/api/public")
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> getDummyData() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "The API is live and working perfectly!");
        response.put("databaseConnected", true);
        response.put("dummyTeams", Arrays.asList("Alpha Coders", "Beta Builders", "Gamma Geeks"));
        
        return ResponseEntity.ok(response);
    }
}
