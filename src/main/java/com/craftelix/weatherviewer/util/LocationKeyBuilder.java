package com.craftelix.weatherviewer.util;

import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.entity.Location;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class LocationKeyBuilder {

    public static String buildLocationKey(LocationApiDto locationApiDto) {
        return buildLocationKey(locationApiDto.getName(), locationApiDto.getLatitude(), locationApiDto.getLongitude());
    }

    public static String buildLocationKey(Location location) {
        return buildLocationKey(location.getName(), location.getLatitude(), location.getLongitude());
    }

    public static String buildLocationKey(String name, BigDecimal latitude, BigDecimal longitude) {
        return name + ":" + latitude + ":" + longitude;
    }
}
