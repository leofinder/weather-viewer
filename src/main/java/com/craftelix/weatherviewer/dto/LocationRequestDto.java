package com.craftelix.weatherviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDto {

    private String name;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String country;

    private String state;

}
