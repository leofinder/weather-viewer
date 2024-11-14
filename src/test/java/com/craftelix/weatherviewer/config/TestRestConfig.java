package com.craftelix.weatherviewer.config;

import com.craftelix.weatherviewer.service.api.OpenWeatherApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TestRestConfig {

    @Bean
    public RestClientConfig restClientConfig() {
        return Mockito.mock(RestClientConfig.class);
    }

    @Bean
    public RestClient restClient() {
        return Mockito.mock(RestClient.class);
    }

    @Bean
    public OpenWeatherApiService openWeatherApiService(RestClient restClient, RestClientConfig restClientConfig) {
        return new OpenWeatherApiService(restClient, restClientConfig);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
