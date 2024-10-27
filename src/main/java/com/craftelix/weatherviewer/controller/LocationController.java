package com.craftelix.weatherviewer.controller;

import com.craftelix.weatherviewer.dto.LocationRequestDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.service.LocationService;
import com.craftelix.weatherviewer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@Slf4j
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addLocation(@RequestBody LocationRequestDto locationRequestDto,
                                                           @CookieValue(value = "sessionId", defaultValue = "") String sessionId) {
        return executeLocationOperation(locationRequestDto, sessionId, locationService::save);
    }

    @PostMapping("/remove")
    public ResponseEntity<Map<String, String>> removeLocation(@RequestBody LocationRequestDto locationRequestDto,
                                                              @CookieValue(value = "sessionId", defaultValue = "") String sessionId) {
        // TODO: проверить, что перед вызовом вызывается фильтр
        return executeLocationOperation(locationRequestDto, sessionId, locationService::delete);
    }

    private ResponseEntity<Map<String, String>> executeLocationOperation(LocationRequestDto locationRequestDto,
                                                                         String sessionId,
                                                                         BiConsumer<LocationRequestDto, User> operation) {
        User user = userService.getUserBySessionId(UUID.fromString(sessionId));
        try {
            operation.accept(locationRequestDto, user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of("message", "Success"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
