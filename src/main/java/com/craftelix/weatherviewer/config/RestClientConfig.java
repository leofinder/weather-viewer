package com.craftelix.weatherviewer.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;

@Slf4j
@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class RestClientConfig {

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Value("${weather.api.data-api-path}")
    private String dataApiPath;

    @Value("${weather.api.geo-api-path}")
    private String geoApiPath;

    @Value("${weather.api.key}")
    private String apiKey;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
