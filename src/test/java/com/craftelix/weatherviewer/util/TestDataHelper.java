package com.craftelix.weatherviewer.util;

import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.dto.location.LocationRequestDto;
import com.craftelix.weatherviewer.entity.Location;
import com.craftelix.weatherviewer.entity.Session;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TestDataHelper {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        return userRepository.save(user);
    }

    public User createDefaultUser() {
        return createUser("testuser", "password");
    }

    public Session createSession(User user, int minutesToExpire) {
        return Session.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .expiresAt(LocalDateTime.now().plusMinutes(minutesToExpire))
                .build();
    }

    public Session createDefaultSession(User user) {
        return createSession(user, 10);
    }

    public Location createLocation(User user, String name, BigDecimal latitude, BigDecimal longitude) {
        return Location.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .userId(user.getId())
                .build();
    }

    public Location createDefaultLocation(User user) {
        return createLocation(user, "TestCity", BigDecimal.valueOf(12.3401), BigDecimal.valueOf(56.7812));
    }

    public LocationRequestDto createLocationRequestDto() {
        LocationRequestDto locationRequestDto = new LocationRequestDto();
        locationRequestDto.setName("TestCity");
        locationRequestDto.setLatitude(BigDecimal.valueOf(12.3401));
        locationRequestDto.setLongitude(BigDecimal.valueOf(56.7812));
        return locationRequestDto;
    }

    public LocationApiDto getLocationApiDto(Location location) {
        LocationApiDto locationApiDto = new LocationApiDto();
        locationApiDto.setName(location.getName());
        locationApiDto.setLatitude(location.getLatitude());
        locationApiDto.setLongitude(location.getLongitude());
        return locationApiDto;
    }
}
