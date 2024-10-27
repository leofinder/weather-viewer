package com.craftelix.weatherviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDto {

    private String name;

    private double latitude;

    private double longitude;

}
