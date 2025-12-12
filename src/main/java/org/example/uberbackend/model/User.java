package org.example.uberbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "User ID (auto-generated)", example = "507f1f77bcf86cd799439011")
    private String id;

    @Indexed(unique = true)
    @Schema(description = "Username (must be unique)", example = "passenger1")
    private String username;

    @Schema(description = "Password (will be encrypted)", example = "password123")
    private String password;

    @Schema(description = "User role: ROLE_USER (passenger) or ROLE_DRIVER (driver)", example = "ROLE_USER", allowableValues = {"ROLE_USER", "ROLE_DRIVER"})
    private String role;
}
