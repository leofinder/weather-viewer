package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.config.RestClientConfig;
import com.craftelix.weatherviewer.config.TestRestConfig;
import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.dto.api.WeatherApiDto;
import com.craftelix.weatherviewer.service.api.OpenWeatherApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestRestConfig.class, OpenWeatherApiService.class })
public class OpenWeatherApiServiceTest {

    private static ResponseSpec responseSpecMock;

    @Autowired
    private OpenWeatherApiService openWeatherApiService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUpClass(@Autowired RestClient restClient, @Autowired RestClientConfig restClientConfig) {
        when(restClientConfig.getGeoApiPath()).thenReturn("/geo/1.0/direct");
        when(restClientConfig.getDataApiPath()).thenReturn("/data/2.5/weather");
        when(restClientConfig.getLimit()).thenReturn("5");
        when(restClientConfig.getApiKey()).thenReturn("mockApiKey");

        RequestHeadersUriSpec requestHeadersUriSpecMock = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec requestHeadersSpecMock = mock(RequestHeadersSpec.class);
        responseSpecMock = mock(ResponseSpec.class);

        when(restClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
    }

    @Test
    void testFindLocationsByName() throws JsonProcessingException {
        String name = "Java";
        String responseBody = """
                [
                    {
                        "name": "Java",
                        "local_names": {
                            "id": "Jawa",
                            "nl": "Java",
                            "en": "Java",
                            "fr": "Java"
                        },
                        "lat": -7.327969400000001,
                        "lon": 109.6139109880134,
                        "country": "ID"
                    },
                    {
                        "name": "Java",
                        "lat": 36.8359709,
                        "lon": -79.2277979,
                        "country": "US",
                        "state": "Virginia"
                    }
                ]
                """;

        TypeReference<List<LocationApiDto>> typeReference = new TypeReference<>() { };
        List<LocationApiDto> mockResponse = objectMapper.readValue(responseBody, typeReference);

        ParameterizedTypeReference<List<LocationApiDto>> parameterizedTypeReference = new ParameterizedTypeReference<>() { };
        when(responseSpecMock.body(parameterizedTypeReference)).thenReturn(mockResponse);

        List<LocationApiDto> result = openWeatherApiService.findLocationsByName(name);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        LocationApiDto firstLocation = result.get(0);
        LocationApiDto secondLocation = result.get(1);

        assertThat(firstLocation.getName()).isEqualTo("Java");
        assertThat(firstLocation.getCountry()).isEqualTo("ID");
        assertThat(firstLocation.getState()).isNull();
        assertThat(firstLocation.getLatitude()).isEqualTo(new BigDecimal("-7.3280"));
        assertThat(firstLocation.getLongitude()).isEqualTo(new BigDecimal("109.6139"));

        assertThat(secondLocation.getName()).isEqualTo("Java");
        assertThat(secondLocation.getCountry()).isEqualTo("US");
        assertThat(secondLocation.getState()).isEqualTo("Virginia");
        assertThat(secondLocation.getLatitude()).isEqualTo(new BigDecimal("36.8360"));
        assertThat(secondLocation.getLongitude()).isEqualTo(new BigDecimal("-79.2278"));
    }

    @Test
    void testGetWeatherData() throws JsonProcessingException {
        BigDecimal latitude = new BigDecimal("-7.3280");
        BigDecimal longitude = new BigDecimal("109.6139");

        String responseBody = """
                {
                    "coord": {
                        "lon": 109.6139,
                        "lat": -7.328
                    },
                    "weather": [
                        {
                            "id": 804,
                            "main": "Clouds",
                            "description": "overcast clouds",
                            "icon": "04n"
                        }
                    ],
                    "base": "stations",
                    "main": {
                        "temp": 22.35,
                        "feels_like": 23.12,
                        "temp_min": 22.35,
                        "temp_max": 22.35,
                        "pressure": 1011,
                        "humidity": 95,
                        "sea_level": 1011,
                        "grnd_level": 950
                    },
                    "visibility": 10000,
                    "wind": {
                        "speed": 0.48,
                        "deg": 18,
                        "gust": 0.39
                    },
                    "clouds": {
                        "all": 99
                    },
                    "dt": 1731257750,
                    "sys": {
                        "type": 2,
                        "id": 2034507,
                        "country": "ID",
                        "sunrise": 1731190374,
                        "sunset": 1731235088
                    },
                    "timezone": 25200,
                    "id": 1630366,
                    "name": "Purbalingga",
                    "cod": 200
                }
                """;

        WeatherApiDto mockResponse = objectMapper.readValue(responseBody, WeatherApiDto.class);

        when(responseSpecMock.body(WeatherApiDto.class)).thenReturn(mockResponse);

        WeatherApiDto result = openWeatherApiService.getWeatherData(latitude, longitude);

        assertThat(result).isNotNull();
        assertThat(result.getMain()).isEqualTo("Clouds");
        assertThat(result.getDescription()).isEqualTo("overcast clouds");
        assertThat(result.getIcon()).isEqualTo("04n");
        assertThat(result.getTemp()).isEqualTo(22);
        assertThat(result.getFeelsLike()).isEqualTo(23);
    }
}