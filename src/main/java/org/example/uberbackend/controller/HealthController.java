package org.example.uberbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check APIs", description = "Application and database health check endpoints")
public class HealthController {

    @Autowired
    private MongoTemplate mongoTemplate;

    // Acha hua ye implement kiya, it helpmed me A LOT :)

    // Health check 1: Check if backend is running
    @GetMapping("/ping")
    @Operation(
        summary = "Ping health check",
        description = "Simple endpoint to verify the application is running"
    )
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Backend is running! 🚀");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    // Health check 2: Check MongoDB connection
    @GetMapping("/db")
    @Operation(
        summary = "Database connectivity check",
        description = "Verifies MongoDB connection is healthy and accessible"
    )
    public ResponseEntity<Map<String, String>> checkDatabase() {
        Map<String, String> response = new HashMap<>();
        try {
            String dbName = mongoTemplate.getDb().getName();
            mongoTemplate.getDb().runCommand(new org.bson.Document("ping", 1));

            response.put("status", "SUCCESS");
            response.put("message", "MongoDB connected successfully! ✅");
            response.put("database", dbName);
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "FAILED");
            response.put("message", "MongoDB connection failed! ❌");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}

