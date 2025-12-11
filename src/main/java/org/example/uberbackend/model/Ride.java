package org.example.uberbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    @Id
    private String id;

    private String passengerUsername;
    private String driverUsername;

    private String pickupLocation;
    private String dropLocation;

    private Double fare;
    private Double distanceKm;

    private String status;

    private LocalDate createdDate;
    private LocalDateTime createdAt;
}

