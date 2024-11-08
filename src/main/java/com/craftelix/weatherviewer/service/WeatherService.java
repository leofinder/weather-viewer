package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.dto.api.WeatherApiDto;

import java.math.BigDecimal;
import java.util.List;

public interface WeatherService {

    List<LocationApiDto> findLocationsByName(String city);

    WeatherApiDto getWeatherData(BigDecimal latitude, BigDecimal longitude);
}
