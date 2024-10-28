package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.config.RestClientConfig;
import com.craftelix.weatherviewer.dto.api.LocationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class WeatherService {

    private final RestClient restClient;

    private final RestClientConfig restClientConfig;

    @Autowired
    public WeatherService(RestClient restClient, RestClientConfig restClientConfig) {
        this.restClient = restClient;
        this.restClientConfig = restClientConfig;
    }

    public List<LocationDto> findLocationsByName(String city) {
        int limit = 5;

        List<LocationDto> locations = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(restClientConfig.getGeoApiPath())
                        .queryParam("q", city)
                        .queryParam("limit", limit)
                        .queryParam("appid", restClientConfig.getApiKey())
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return locations;
    }

}
