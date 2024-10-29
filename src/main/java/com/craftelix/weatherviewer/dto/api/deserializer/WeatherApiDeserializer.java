package com.craftelix.weatherviewer.dto.api.deserializer;

import com.craftelix.weatherviewer.dto.api.WeatherApiDto;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class WeatherApiDeserializer extends StdDeserializer<WeatherApiDto> {

    public WeatherApiDeserializer() {
        this(null);
    }

    protected WeatherApiDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public WeatherApiDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode weatherNode = node.get("weather");
        JsonNode mainNode = node.get("main");

        String main = weatherNode.get(0).get("main").asText();
        String description = weatherNode.get(0).get("description").asText();
        String icon = String.format("https://openweathermap.org/img/wn/%s@2x.png", weatherNode.get(0).get("icon").asText());

        int temp = mainNode.get("temp").asInt();
        int feelsLike = mainNode.get("feels_like").asInt();

        return WeatherApiDto.builder()
                .main(main)
                .description(description)
                .icon(icon)
                .temp(temp)
                .feelsLike(feelsLike)
                .build();
    }
}