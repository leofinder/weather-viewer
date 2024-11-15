package com.craftelix.weatherviewer.dto.api;

import com.craftelix.weatherviewer.dto.api.deserializer.OpenWeatherApiDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = OpenWeatherApiDeserializer.class)
public class WeatherApiDto {

    private String main;

    private String description;

    private String icon;

    private int temp;

    private int feelsLike;

}
