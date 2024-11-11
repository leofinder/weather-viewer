package com.craftelix.weatherviewer.repository;

import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryCustomImpl implements LocationRepositoryCustom {

    private final JdbcClient jdbcClient;

    @Override
    public List<Location> findUserLocationsByParams(Long userId, List<LocationApiDto> locationsApiDto) {
        List<String> names = locationsApiDto.stream()
                .map(LocationApiDto::getName)
                .distinct()
                .toList();

        List<BigDecimal> latitudes = locationsApiDto.stream()
                .map(LocationApiDto::getLatitude)
                .toList();

        List<BigDecimal> longitudes = locationsApiDto.stream()
                .map(LocationApiDto::getLongitude)
                .toList();

        String query = """
            SELECT l.*
            FROM locations l
            WHERE l.user_id = :userId
              AND l.name IN (:names)
              AND l.latitude IN (:latitudes)
              AND l.longitude IN (:longitudes)
            """;

        return jdbcClient.sql(query)
                .param("userId", userId)
                .param("names", names)
                .param("latitudes", latitudes)
                .param("longitudes", longitudes)
                .query(Location.class)
                .list();
    }

}