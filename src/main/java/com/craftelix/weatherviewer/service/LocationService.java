package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.LocationRequestDto;
import com.craftelix.weatherviewer.dto.openweather.LocationDto;
import com.craftelix.weatherviewer.entity.Location;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.mapper.LocationMapper;
import com.craftelix.weatherviewer.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private final WeatherService weatherService;

    private final LocationMapper locationMapper;

    public void save(LocationRequestDto locationRequestDto, User user) {
        Location location = locationMapper.toEntity(locationRequestDto);
        location.setUserId(user.getId());
        locationRepository.save(location);
    }

    public void delete(LocationRequestDto locationRequestDto, User user) {
        Location location = locationMapper.toEntity(locationRequestDto);
        location.setUserId(user.getId());
        locationRepository.findByLocationInfo(location).ifPresent(
                locationRepository::delete
        );
    }

    public List<LocationDto> findLocationsByUser(User user) {
        List<Location> userLocations = locationRepository.findByUserId(user.getId());
        return locationMapper.toDto(userLocations);
    }

    public List<LocationDto> markLocationsAsAddedForUser(List<LocationDto> locationsDto, User user) {
        List<Location> userLocations = locationRepository.findByUserId(user.getId());

        for (LocationDto locationDto : locationsDto) {
            Location location = locationMapper.toEntity(locationDto);
            location.setUserId(user.getId());
            locationDto.setAdded(userLocations.contains(location));
        }
        return locationsDto;
    }
}
