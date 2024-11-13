package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.config.TestJdbcConfig;
import com.craftelix.weatherviewer.config.TestRestConfig;
import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.dto.location.LocationRequestDto;
import com.craftelix.weatherviewer.dto.location.LocationWithUserStatusDto;
import com.craftelix.weatherviewer.dto.user.UserDto;
import com.craftelix.weatherviewer.entity.Location;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.BadRequestException;
import com.craftelix.weatherviewer.mapper.LocationMapperImpl;
import com.craftelix.weatherviewer.mapper.UserMapper;
import com.craftelix.weatherviewer.mapper.UserMapperImpl;
import com.craftelix.weatherviewer.repository.LocationRepository;
import com.craftelix.weatherviewer.repository.UserRepository;
import com.craftelix.weatherviewer.service.api.OpenWeatherApiService;
import com.craftelix.weatherviewer.service.api.WeatherService;
import com.craftelix.weatherviewer.util.TestDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class, LocationService.class, LocationRepository.class, LocationMapperImpl.class,
        UserRepository.class, UserMapperImpl.class, TestDataHelper.class, OpenWeatherApiService.class, TestRestConfig.class})
@TestPropertySource(properties = {
        "rest-client.connection-timeout=5000",
        "rest-client.connection-request-timeout=5000"
})
@Transactional
class LocationServiceTest {

    @Autowired
    private LocationService locationService;
    
    @Autowired
    @Qualifier("openWeatherApiService")
    private WeatherService weatherService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TestDataHelper testDataHelper;

    private User user;
    
    private UserDto userDto;
    
    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();
        userRepository.deleteAll();
        
        user = testDataHelper.createDefaultUser();
        userDto = userMapper.toDto(user);
    }

    @Test
    void shouldSaveLocation() {
        LocationRequestDto locationRequestDto = testDataHelper.createLocationRequestDto();
        locationService.save(locationRequestDto, userDto);

        List<Location> savedLocations = locationRepository.findByUserId(user.getId());

        assertThat(savedLocations).hasSize(1);

        Location savedLocation = savedLocations.get(0);
        assertThat(savedLocation.getName()).isEqualTo(locationRequestDto.getName());
        assertThat(savedLocation.getUserId()).isEqualTo(user.getId());
        assertThat(savedLocation.getLatitude()).isEqualTo(locationRequestDto.getLatitude());
        assertThat(savedLocation.getLongitude()).isEqualTo(locationRequestDto.getLongitude());
    }

    @Test
    void shouldDeleteLocation() {
        Location location = testDataHelper.createDefaultLocation(user);
        locationRepository.save(location);

        locationService.delete(location.getId(), userDto);

        Optional<Location> deletedLocation = locationRepository.findById(location.getId());
        assertThat(deletedLocation).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentLocation() {
        Long nonexistentId = 999L;

        assertThatThrownBy(() -> locationService.delete(nonexistentId, userDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowExceptionWhenDeletingOtherUsersLocation() {
        User otherUser = testDataHelper.createUser("otheruser", "otherpassword");
        Location locationOtherUser = testDataHelper.createDefaultLocation(otherUser);
        Location savedLocationOtherUser = locationRepository.save(locationOtherUser);

        assertThatThrownBy(() -> locationService.delete(savedLocationOtherUser.getId(), userDto))
                .isInstanceOf(BadRequestException.class);

        Optional<Location> foundLocation = locationRepository.findById(savedLocationOtherUser.getId());
        assertThat(foundLocation).isPresent();
    }

    @Test
    void shouldGetLocationsWithUserStatus() {
        Location location = testDataHelper.createDefaultLocation(user);
        locationRepository.save(location);

        LocationApiDto locationApiDto = testDataHelper.getLocationApiDto(location);

        List<LocationWithUserStatusDto> result = locationService.getLocationsWithUserStatus(Collections.singletonList(locationApiDto), userDto);

        assertThat(result).hasSize(1);

        LocationWithUserStatusDto dto = result.get(0);
        assertThat(dto.getLocation().getName()).isEqualTo(location.getName());
        assertThat(dto.getLocation().getLatitude()).isEqualTo(location.getLatitude());
        assertThat(dto.getLocation().getLongitude()).isEqualTo(location.getLongitude());
        assertThat(dto.isAdded()).isTrue();
    }

}
