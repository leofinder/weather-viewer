package com.craftelix.weatherviewer.controller;

import com.craftelix.weatherviewer.dto.LocationRequestDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("")
    public ResponseEntity<Map<String, String>> addLocation(@RequestBody LocationRequestDto locationRequestDto,
                                                           HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        try {
            locationService.save(locationRequestDto, user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", "Success"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Map<String, String>> removeLocation(@PathVariable("locationId") Long locationId,
                                                              HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        try {
            locationService.delete(locationId, user);
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
