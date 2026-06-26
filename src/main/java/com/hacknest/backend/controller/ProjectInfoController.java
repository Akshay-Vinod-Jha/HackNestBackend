package com.hacknest.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class ProjectInfoController {

    @GetMapping
    public Map<String, Object> getProjectInfo() {
        Map<String, Object> info = new HashMap<>();
        
        info.put("projectName", "HackNest Backend");
        info.put("version", "1.0.0");
        info.put("status", "Active and running");
        info.put("framework", "Spring Boot 3.5.16");
        info.put("language", "Java 21");
        
        // Add core dependencies/features active in the project
        info.put("features", List.of(
            "Spring Web (REST API)",
            "Spring Data MongoDB (Database)",
            "Spring Security (Authentication & Authorization)",
            "Spring Validation"
        ));
        
        return info;
    }
}
