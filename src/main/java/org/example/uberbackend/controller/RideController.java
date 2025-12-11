package org.example.uberbackend.controller;

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
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping
    public Ride createRide(@AuthenticationPrincipal UserDetails user,
                          @RequestBody Ride ride) {
        return rideService.createRide(user.getUsername(), ride);
    }

    @PostMapping("/accept/{id}")
    public Ride acceptRide(@AuthenticationPrincipal UserDetails driver,
                          @PathVariable String id) {
        boolean isDriver = driver.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_DRIVER"));

        if (!isDriver) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only drivers can accept rides");
        }

        return rideService.acceptRide(id, driver.getUsername());
    }

    @PostMapping("/complete/{id}")
    public Ride completeRide(@AuthenticationPrincipal UserDetails user,
                            @PathVariable String id) {
        return rideService.completeRide(id, user.getUsername());
    }

    @GetMapping
    public List<Ride> getAllRides() {
        return rideService.getAllRides();
    }


    // API 1: Search by keyword
    @GetMapping("/search")
    public List<Ride> searchRides(@RequestParam String text) {
        return rideService.searchRides(text);
    }

    // API 2: Filter by distance
    @GetMapping("/filter-distance")
    public List<Ride> filterByDistance(@RequestParam Double min, @RequestParam Double max) {
        return rideService.filterByDistance(min, max);
    }

    // API 3: Filter by date range
    @GetMapping("/filter-date-range")
    public List<Ride> filterByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return rideService.filterByDateRange(start, end);
    }

    // API 4: Sort by fare
    @GetMapping("/sort")
    public List<Ride> sortByFare(@RequestParam(defaultValue = "asc") String order) {
        return rideService.sortByFare(order);
    }

    // API 5: Get user's rides
    @GetMapping("/user/{userId}")
    public List<Ride> getRidesByUser(@PathVariable String userId) {
        return rideService.getRidesByUser(userId);
    }

    // API 6: Get user's rides by status
    @GetMapping("/user/{userId}/status/{status}")
    public List<Ride> getRidesByUserAndStatus(@PathVariable String userId,
                                              @PathVariable String status) {
        return rideService.getRidesByUserAndStatus(userId, status);
    }

    // API 7: Driver's active rides
    @GetMapping("/driver/{driverId}/active-rides")
    public List<Ride> getDriverActiveRides(@PathVariable String driverId) {
        return rideService.getDriverActiveRides(driverId);
    }

    // API 8: Filter by status + keyword
    @GetMapping("/filter-status")
    public List<Ride> filterByStatusAndKeyword(@RequestParam String status,
                                               @RequestParam String search) {
        return rideService.filterByStatusAndKeyword(status, search);
    }

    // API 9: Advanced search with pagination
    @GetMapping("/advanced-search")
    public List<Ride> advancedSearch(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return rideService.advancedSearch(search, status, sort, order, page, size);
    }

    // API 10: Rides by specific date
    @GetMapping("/date/{date}")
    public List<Ride> getRidesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return rideService.getRidesByDate(date);
    }
}

