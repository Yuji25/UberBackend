package org.example.uberbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Ride ID (auto-generated)", example = "507f1f77bcf86cd799439011")
    private String id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Passenger username (set automatically from JWT)", example = "passenger1")
    private String passengerUsername;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Driver username (set when ride is accepted)", example = "driver1")
    private String driverUsername;

    @Schema(description = "Pickup location", example = "Downtown Station")
    private String pickupLocation;

    @Schema(description = "Drop-off location", example = "Airport Terminal 2")
    private String dropLocation;

    @Schema(description = "Ride fare in currency units", example = "25.50")
    private Double fare;

    @Schema(description = "Distance in kilometers", example = "15.3")
    private Double distanceKm;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Ride status (REQUESTED, ACCEPTED, COMPLETED)", example = "REQUESTED")
    private String status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Date when ride was created", example = "2025-12-12")
    private LocalDate createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Timestamp when ride was created", example = "2025-12-12T10:30:00")
    private LocalDateTime createdAt;
}

