package com.craftelix.weatherviewer.dto.api;

import com.craftelix.weatherviewer.dto.api.deserializer.BigDecimalFourDecimalPlacesDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationApiDto {

    private String name;

    @JsonProperty("lat")
    @JsonDeserialize(using = BigDecimalFourDecimalPlacesDeserializer.class)
    private BigDecimal latitude;

    @JsonProperty("lon")
    @JsonDeserialize(using = BigDecimalFourDecimalPlacesDeserializer.class)
    private BigDecimal longitude;

    private String country;

    private String state;

}
