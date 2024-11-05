package com.craftelix.weatherviewer.controller.rest;

import com.craftelix.weatherviewer.dto.LocationRequestDto;
import com.craftelix.weatherviewer.dto.UserDto;
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
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        locationService.save(locationRequestDto, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Success"));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Map<String, String>> removeLocation(@PathVariable("locationId") Long locationId,
                                                              HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        locationService.delete(locationId, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("message", "Success"));

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", ex.getMessage()));
    }

}
