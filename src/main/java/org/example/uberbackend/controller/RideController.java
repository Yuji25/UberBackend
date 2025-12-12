package org.example.uberbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.uberbackend.model.Ride;
import org.example.uberbackend.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
@Tag(name = "Core Ride APIs", description = "Core ride management operations - create, accept, complete, and query rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping
    @Operation(
        summary = "Create new ride",
        description = "Passenger creates a ride request with pickup/drop locations, fare, and distance. Status set to REQUESTED."
    )
    public Ride createRide(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user,
        @RequestBody Ride ride
    ) {
        return rideService.createRide(user.getUsername(), ride);
    }

    @PostMapping("/accept/{id}")
    @Operation(
        summary = "Accept ride (Driver only)",
        description = "Driver accepts a REQUESTED ride. Changes status to ACCEPTED and assigns driver. Requires ROLE_DRIVER."
    )
    public Ride acceptRide(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetails driver,
        @Parameter(description = "Ride ID to accept") @PathVariable String id
    ) {
        boolean isDriver = driver.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_DRIVER"));

        if (!isDriver) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only drivers can accept rides");
        }

        return rideService.acceptRide(id, driver.getUsername());
    }

    @PostMapping("/complete/{id}")
    @Operation(
        summary = "Complete ride",
        description = "Marks ride as COMPLETED. Only assigned driver or passenger can complete. Changes status from ACCEPTED to COMPLETED."
    )
    public Ride completeRide(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user,
        @Parameter(description = "Ride ID to complete") @PathVariable String id
    ) {
        return rideService.completeRide(id, user.getUsername());
    }

    @GetMapping
    @Operation(
        summary = "Get all rides",
        description = "Retrieves all rides from database regardless of status or user"
    )
    public List<Ride> getAllRides() {
        return rideService.getAllRides();
    }

    // ===== ADVANCED QUERY OPERATIONS =====
    // Hard tha implement karna :-D

    // API 1: Search by keyword
    @GetMapping("/search")
    @Operation(
        summary = "Search rides by keyword",
        description = "Search rides by keyword in pickup OR drop location (case-insensitive regex). Uses MongoTemplate OR query."
    )
    public List<Ride> searchRides(
        @Parameter(description = "Search keyword (e.g., 'Airport', 'Downtown')") @RequestParam String text
    ) {
        return rideService.searchRides(text);
    }

    // API 2: Filter by distance
    @GetMapping("/filter-distance")
    @Operation(
        summary = "Filter rides by distance range",
        description = "Filter rides where distance is between min and max kilometers. Validates min <= max and no negative values."
    )
    public List<Ride> filterByDistance(
        @Parameter(description = "Minimum distance in km") @RequestParam Double min,
        @Parameter(description = "Maximum distance in km") @RequestParam Double max
    ) {
        return rideService.filterByDistance(min, max);
    }

    // API 3: Filter by date range
    @GetMapping("/filter-date-range")
    @Operation(
        summary = "Filter rides by date range",
        description = "Get rides created between start and end dates (inclusive). Uses LocalDate comparison."
    )
    public List<Ride> filterByDateRange(
        @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return rideService.filterByDateRange(start, end);
    }

    // API 4: Sort by fare
    @GetMapping("/sort")
    @Operation(
        summary = "Sort rides by fare",
        description = "Sort all rides by fare amount. Supports ascending (cheapest first) or descending (most expensive first)."
    )
    public List<Ride> sortByFare(
        @Parameter(description = "Sort order: 'asc' or 'desc'") @RequestParam(defaultValue = "asc") String order
    ) {
        return rideService.sortByFare(order);
    }

    // API 5: Get user's rides
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get user's rides",
        description = "Get all rides for a specific passenger. Uses equality query on passengerUsername."
    )
    public List<Ride> getRidesByUser(
        @Parameter(description = "Passenger username") @PathVariable String userId
    ) {
        return rideService.getRidesByUser(userId);
    }

    // API 6: Get user's rides by status
    @GetMapping("/user/{userId}/status/{status}")
    @Operation(
        summary = "Get user's rides by status",
        description = "Get rides for a passenger filtered by status. Combines user equality AND status equality."
    )
    public List<Ride> getRidesByUserAndStatus(
        @Parameter(description = "Passenger username") @PathVariable String userId,
        @Parameter(description = "Ride status (REQUESTED, ACCEPTED, COMPLETED)") @PathVariable String status
    ) {
        return rideService.getRidesByUserAndStatus(userId, status);
    }

    // API 7: Driver's active rides
    @GetMapping("/driver/{driverId}/active-rides")
    @Operation(
        summary = "Get driver's active rides",
        description = "Get driver's rides with status REQUESTED or ACCEPTED (excluding COMPLETED). Uses IN query for multiple statuses."
    )
    public List<Ride> getDriverActiveRides(
        @Parameter(description = "Driver username") @PathVariable String driverId
    ) {
        return rideService.getDriverActiveRides(driverId);
    }

    // API 8: Filter by status + keyword
    @GetMapping("/filter-status")
    @Operation(
        summary = "Filter by status and keyword",
        description = "Complex query combining status equality AND keyword search (pickup OR drop). Demonstrates AND + OR query combination."
    )
    public List<Ride> filterByStatusAndKeyword(
        @Parameter(description = "Ride status") @RequestParam String status,
        @Parameter(description = "Search keyword") @RequestParam String search
    ) {
        return rideService.filterByStatusAndKeyword(status, search);
    }

    // API 9: Advanced search with pagination
    @GetMapping("/advanced-search")
    @Operation(
        summary = "Advanced search with pagination",
        description = "Full-featured search with optional keyword, status filter, sorting, and pagination. All parameters optional."
    )
    public List<Ride> advancedSearch(
        @Parameter(description = "Search keyword (optional)") @RequestParam(required = false) String search,
        @Parameter(description = "Filter by status (optional)") @RequestParam(required = false) String status,
        @Parameter(description = "Sort field (optional, default: createdAt)") @RequestParam(required = false, defaultValue = "createdAt") String sort,
        @Parameter(description = "Sort order (optional, default: asc)") @RequestParam(required = false, defaultValue = "asc") String order,
        @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size
    ) {
        return rideService.advancedSearch(search, status, sort, order, page, size);
    }

    // API 10: Rides by specific date
    @GetMapping("/date/{date}")
    @Operation(
        summary = "Get rides by specific date",
        description = "Get all rides created on a specific date. Uses LocalDate equality."
    )
    public List<Ride> getRidesByDate(
        @Parameter(description = "Date (YYYY-MM-DD)") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return rideService.getRidesByDate(date);
    }
}

