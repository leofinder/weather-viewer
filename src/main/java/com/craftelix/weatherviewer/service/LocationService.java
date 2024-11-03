package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.LocationRequestDto;
import com.craftelix.weatherviewer.dto.LocationResponseDto;
import com.craftelix.weatherviewer.dto.LocationWithUserStatusDto;
import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.entity.Location;
import com.craftelix.weatherviewer.mapper.LocationMapper;
import com.craftelix.weatherviewer.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        locationRepository.save(location);
    }

    public void delete(Long id, UserDto user) {
        locationRepository.findByIdAndUserId(id, user.getId()).ifPresent(
                locationRepository::delete
        );
    }

    public List<LocationResponseDto> getUserLocationsWithWeatherData(UserDto user) {
        List<LocationResponseDto> userLocationsDto = getUserLocations(user);
        for (LocationResponseDto location : userLocationsDto) {
            location.setWeatherApi(weatherService.getWeatherData(location.getLatitude(), location.getLongitude()));
        }
        return userLocationsDto;
    }

    private List<LocationResponseDto> getUserLocations(UserDto user) {
        List<Location> userLocations = locationRepository.findByUserId(user.getId());
        return locationMapper.toDto(userLocations);
    }

    public List<LocationWithUserStatusDto> setUserStatusForLocations(List<LocationApiDto> locationsApiDto, UserDto user) {
        List<LocationWithUserStatusDto> locationsWithUserStatusDto = new ArrayList<>();
        List<Location> userLocations = locationRepository.findByUserId(user.getId());

        for (LocationApiDto locationApiDto : locationsApiDto) {
            LocationWithUserStatusDto locationWithUserStatusDto = getLocationWithUserStatus(locationApiDto, userLocations, user);
            locationsWithUserStatusDto.add(locationWithUserStatusDto);
        }

        return locationsWithUserStatusDto;
    }

    private LocationWithUserStatusDto getLocationWithUserStatus(LocationApiDto locationApiDto,  List<Location> userLocations, UserDto user) {
        Location location = locationMapper.toEntity(locationApiDto);
        location.setUserId(user.getId());

        return LocationWithUserStatusDto.builder()
                .location(locationApiDto)
                .isAdded(userLocations.contains(location))
                .build();
    }
}
