package org.example.uberbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.Document;
import org.example.uberbackend.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics APIs", description = "MongoDB aggregation pipelines for business analytics and reporting")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    // API 1: Driver earnings
    @GetMapping("/driver/{driver}/earnings")
    @Operation(
        summary = "Get driver total earnings",
        description = "Calculate total earnings for a driver from COMPLETED rides only. Uses SUM aggregation on fare field."
    )
    public Double getDriverEarnings(
        @Parameter(description = "Driver username") @PathVariable String driver
    ) {
        return analyticsService.getTotalEarnings(driver);
    }

    // API 2: Rides per day
    @GetMapping("/rides-per-day")
    @Operation(
        summary = "Get rides per day",
        description = "Group rides by date and count. Returns array of {date, ridesCount} sorted by date descending. Uses GROUP BY aggregation."
    )
    public List<Document> getRidesPerDay() {
        return analyticsService.getRidesPerDay();
    }

    // API 3: Driver summary
    @GetMapping("/driver/{driverId}/summary")
    @Operation(
        summary = "Get driver summary",
        description = "Comprehensive driver statistics: total rides, completed rides, average distance, total fare. Uses complex aggregation with conditional SUM."
    )
    public Document getDriverSummary(
        @Parameter(description = "Driver username") @PathVariable String driverId
    ) {
        return analyticsService.getDriverSummary(driverId);
    }

    // API 4: User spending
    @GetMapping("/user/{userId}/spending")
    @Operation(
        summary = "Get user spending",
        description = "Calculate total spending and completed rides for a passenger. Only counts COMPLETED rides. Uses MATCH + GROUP aggregation."
    )
    public Document getUserSpending(
        @Parameter(description = "Passenger username") @PathVariable String userId
    ) {
        return analyticsService.getUserSpending(userId);
    }

    // API 5: Status summary
    @GetMapping("/status-summary")
    @Operation(
        summary = "Get status summary",
        description = "Count rides grouped by status (REQUESTED, ACCEPTED, COMPLETED). Returns array of {status, ridesCount}. Uses GROUP BY status."
    )
    public List<Document> getStatusSummary() {
        return analyticsService.getStatusSummary();
    }
}

