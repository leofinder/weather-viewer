package com.craftelix.weatherviewer.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Slf4j
@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class RestClientConfig {

    @Value("${rest-client.connection-timeout}")
    private int connectionTimeout;

    @Value("${rest-client.connection-request-timeout}")
    private int connectionRequestTimeout;

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Value("${weather.api.data-api-path}")
    private String dataApiPath;

    @Value("${weather.api.geo-api-path}")
    private String geoApiPath;

    @Value("${weather.api.limit}")
    private String limit;

    @Value("${weather.api.units}")
    private String units;

    @Value("${weather.api.key}")
    private String apiKey;

    @Bean
    public RestClient restClient() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(clientHttpRequestFactory)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
