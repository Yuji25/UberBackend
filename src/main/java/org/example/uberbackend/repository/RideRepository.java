package org.example.uberbackend.repository;

import org.example.uberbackend.model.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RideRepository extends MongoRepository<Ride, String> {
    List<Ride> findByPassengerUsername(String username);
    List<Ride> findByDriverUsername(String username);
    List<Ride> findByStatus(String status);
}

