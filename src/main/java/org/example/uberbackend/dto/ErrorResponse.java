package org.example.uberbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response object")
public class ErrorResponse {

    @Schema(description = "Timestamp of the error", example = "2025-12-12T10:30:00")
    private String timestamp;

    @Schema(description = "HTTP status code", example = "500")
    private int status;

    @Schema(description = "Error type", example = "Internal Server Error")
    private String error;

    @Schema(description = "Error message", example = "Username already exists")
    private String message;
}

