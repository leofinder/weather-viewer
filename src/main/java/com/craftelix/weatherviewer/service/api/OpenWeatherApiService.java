package com.craftelix.weatherviewer.service.api;

import com.craftelix.weatherviewer.config.RestClientConfig;
import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.dto.api.WeatherApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenWeatherApiService implements WeatherService {

    private final RestClient restClient;

    private final RestClientConfig restClientConfig;

    public List<LocationApiDto> findLocationsByName(String city) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(restClientConfig.getGeoApiPath())
                        .queryParam("q", city)
                        .queryParam("limit", restClientConfig.getLimit())
                        .queryParam("appid", restClientConfig.getApiKey())
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() { });
    }

    public WeatherApiDto getWeatherData(BigDecimal latitude, BigDecimal longitude) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(restClientConfig.getDataApiPath())
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("appid", restClientConfig.getApiKey())
                        .queryParam("units", restClientConfig.getUnits())
                        .build())
                .retrieve()
                .body(WeatherApiDto.class);
    }

}