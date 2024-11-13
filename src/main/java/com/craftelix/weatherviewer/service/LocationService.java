package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.dto.location.LocationRequestDto;
import com.craftelix.weatherviewer.dto.location.LocationWeatherDto;
import com.craftelix.weatherviewer.dto.location.LocationWithUserStatusDto;
import com.craftelix.weatherviewer.dto.user.UserDto;
import com.craftelix.weatherviewer.entity.Location;
import com.craftelix.weatherviewer.exception.BadRequestException;
import com.craftelix.weatherviewer.exception.LocationAlreadyExistException;
import com.craftelix.weatherviewer.mapper.LocationMapper;
import com.craftelix.weatherviewer.repository.LocationRepository;
import com.craftelix.weatherviewer.service.api.WeatherService;
import com.craftelix.weatherviewer.util.LocationKeyBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private final WeatherService weatherService;

    private final LocationMapper locationMapper;

    public void save(LocationRequestDto locationRequestDto, UserDto user) {
        Location location = locationMapper.toEntity(locationRequestDto);
        location.setUserId(user.getId());
        try {
            locationRepository.save(location);
        } catch (DbActionExecutionException e) {
            if (e.getCause() instanceof DuplicateKeyException) {
                log.warn("Duplicate location detected: '{}', latitude {}, longitude {} for user '{}'",
                        location.getName(), location.getLatitude(), location.getLongitude(), user.getUsername());
                throw new LocationAlreadyExistException(String.format("Location %s, latitude %s, longitude %s already exists for user %s",
                        location.getName(), location.getLatitude(), location.getLongitude(), user.getUsername()));
            }
            log.error("Unexpected error while saving location '{}': {}", location.getName(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id, UserDto user) {
        locationRepository.findByIdAndUserId(id, user.getId()).ifPresentOrElse(
                locationRepository::delete,
                () -> {
                    log.warn("Failed to delete location: No location with id '{}' found for user '{}'", id, user.getUsername());
                    throw new BadRequestException("Cannot perform operation: invalid input parameters");
                }
        );
    }

    public List<LocationWeatherDto> getUserLocationsWithWeatherData(UserDto user) {
        List<LocationWeatherDto> userLocationsDto = getUserLocations(user);
        for (LocationWeatherDto location : userLocationsDto) {
            location.setWeatherApi(weatherService.getWeatherData(location.getLatitude(), location.getLongitude()));
        }
        return userLocationsDto;
    }

    private List<LocationWeatherDto> getUserLocations(UserDto user) {
        List<Location> userLocations = locationRepository.findByUserId(user.getId());
        return locationMapper.toDto(userLocations);
    }

    public List<LocationWithUserStatusDto> getLocationsWithUserStatus(List<LocationApiDto> locationsApiDto, UserDto user) {
        List<Location> userLocations = new ArrayList<>();
        if (!locationsApiDto.isEmpty()) {
            userLocations = locationRepository.findUserLocationsByParams(user.getId(), locationsApiDto);
        }

        Set<String> userLocationKeys = userLocations.stream()
                .map(LocationKeyBuilder::buildLocationKey)
                .collect(Collectors.toSet());

        return locationsApiDto.stream()
                .map(locationApiDto -> LocationWithUserStatusDto.builder()
                        .location(locationApiDto)
                        .isAdded(userLocationKeys.contains(LocationKeyBuilder.buildLocationKey(locationApiDto)))
                        .build())
                .collect(Collectors.toList());
    }

}
