package com.craftelix.weatherviewer.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDto {

    @NotEmpty(message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Latitude is required.")
    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.0.")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.0.")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required.")
    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.0.")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.0.")
    private BigDecimal longitude;

    private String country;

    private String state;

}
