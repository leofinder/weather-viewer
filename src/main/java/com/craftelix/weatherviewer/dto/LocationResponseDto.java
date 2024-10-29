package com.craftelix.weatherviewer.dto;

import com.craftelix.weatherviewer.dto.api.WeatherApiDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDto {

    private Long id;

    private String name;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String country;

    private String state;

    private WeatherApiDto weatherApi;

}
