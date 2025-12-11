package org.example.uberbackend.service;

import org.example.uberbackend.model.Ride;
import org.example.uberbackend.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Ride createRide(String passengerUsername, Ride ride) {
        ride.setPassengerUsername(passengerUsername);
        ride.setStatus("REQUESTED");
        ride.setCreatedDate(LocalDate.now());
        ride.setCreatedAt(LocalDateTime.now());
        return rideRepository.save(ride);
    }

    @Transactional
    public Ride acceptRide(String rideId, String driverUsername) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new RuntimeException("Ride must be in REQUESTED status");
        }

        ride.setDriverUsername(driverUsername);
        ride.setStatus("ACCEPTED");
        return rideRepository.save(ride);
    }

    public Ride completeRide(String rideId, String username) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new RuntimeException("Ride must be ACCEPTED to complete");
        }


        if (!username.equals(ride.getDriverUsername()) && !username.equals(ride.getPassengerUsername())) {
            throw new RuntimeException("Only the assigned driver or passenger can complete this ride");
        }

        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }


    // API 1: Search rides by pickup OR drop (Regex + Case-insensitive)
    public List<Ride> searchRides(String text) {
        Criteria criteria = new Criteria().orOperator(
            Criteria.where("pickupLocation").regex(text, "i"),
            Criteria.where("dropLocation").regex(text, "i")
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 2: Filter rides by distance range
    public List<Ride> filterByDistance(Double min, Double max) {
        if (min < 0 || max < 0) {
            throw new RuntimeException("Distance values cannot be negative");
        }

        if (min > max) {
            throw new RuntimeException("Minimum distance cannot be greater than maximum distance");
        }

        Criteria criteria = Criteria.where("distanceKm").gte(min).lte(max);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 3: Filter rides by date range
    public List<Ride> filterByDateRange(LocalDate start, LocalDate end) {
        Criteria criteria = Criteria.where("createdDate").gte(start).lte(end);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 4: Sort rides by fare
    public List<Ride> sortByFare(String order) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order)
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;
        Query query = new Query().with(Sort.by(direction, "fare"));
        return mongoTemplate.find(query, Ride.class);
    }

    // API 5: Get rides for user (simple equality)
    public List<Ride> getRidesByUser(String userId) {
        Criteria criteria = Criteria.where("passengerUsername").is(userId);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 6: Get rides for user by status (AND query)
    public List<Ride> getRidesByUserAndStatus(String userId, String status) {
        Criteria criteria = Criteria.where("passengerUsername").is(userId)
                                   .and("status").is(status);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 7: Driver's active rides
    public List<Ride> getDriverActiveRides(String driverId) {
        Criteria criteria = Criteria.where("driverUsername").is(driverId)
                                   .and("status").in("REQUESTED", "ACCEPTED");
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 8: Filter rides by status + keyword (AND + OR combo)
    public List<Ride> filterByStatusAndKeyword(String status, String search) {
        Criteria statusCriteria = Criteria.where("status").is(status);
        Criteria searchCriteria = new Criteria().orOperator(
            Criteria.where("pickupLocation").regex(search, "i"),
            Criteria.where("dropLocation").regex(search, "i")
        );

        Criteria combined = new Criteria().andOperator(statusCriteria, searchCriteria);
        Query query = new Query(combined);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 9: Advanced search with pagination + sorting
    public List<Ride> advancedSearch(String search, String status,
                                    String sortBy, String order,
                                    int page, int size) {
        Criteria criteria = new Criteria();

        if (search != null && !search.isEmpty()) {
            criteria = new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(search, "i"),
                Criteria.where("dropLocation").regex(search, "i")
            );
        }

        if (status != null && !status.isEmpty()) {
            if (search != null && !search.isEmpty()) {
                Criteria searchCrit = new Criteria().orOperator(
                    Criteria.where("pickupLocation").regex(search, "i"),
                    Criteria.where("dropLocation").regex(search, "i")
                );
                criteria = new Criteria().andOperator(
                    Criteria.where("status").is(status),
                    searchCrit
                );
            } else {
                criteria = Criteria.where("status").is(status);
            }
        }

        Query query = new Query(criteria);

        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "asc".equalsIgnoreCase(order)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
            query.with(Sort.by(direction, sortBy));
        }

        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);

        return mongoTemplate.find(query, Ride.class);
    }

    // API 10: Rides on specific date
    public List<Ride> getRidesByDate(LocalDate date) {
        Criteria criteria = Criteria.where("createdDate").is(date);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }
}


