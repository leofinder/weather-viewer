package com.craftelix.weatherviewer.repository;

import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.entity.Location;

import java.util.List;

public interface LocationRepositoryCustom {

    List<Location> findUserLocationsByParams(Long userId, List<LocationApiDto> locationsApiDto);
}
