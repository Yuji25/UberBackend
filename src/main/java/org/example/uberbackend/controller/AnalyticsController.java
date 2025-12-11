package org.example.uberbackend.controller;

import org.bson.Document;
import org.example.uberbackend.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    // API 1: Driver earnings
    @GetMapping("/driver/{driver}/earnings")
    public Double getDriverEarnings(@PathVariable String driver) {
        return analyticsService.getTotalEarnings(driver);
    }

    // API 2: Rides per day
    @GetMapping("/rides-per-day")
    public List<Document> getRidesPerDay() {
        return analyticsService.getRidesPerDay();
    }

    // API 3: Driver summary
    @GetMapping("/driver/{driverId}/summary")
    public Document getDriverSummary(@PathVariable String driverId) {
        return analyticsService.getDriverSummary(driverId);
    }

    // API 4: User spending
    @GetMapping("/user/{userId}/spending")
    public Document getUserSpending(@PathVariable String userId) {
        return analyticsService.getUserSpending(userId);
    }

    // API 5: Status summary
    @GetMapping("/status-summary")
    public List<Document> getStatusSummary() {
        return analyticsService.getStatusSummary();
    }
}

