package com.craftelix.weatherviewer.controller.rest;

import com.craftelix.weatherviewer.dto.LocationRequestDto;
import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.exception.LocationValidationException;
import com.craftelix.weatherviewer.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("")
    public ResponseEntity<Map<String, String>> addLocation(@Valid @RequestBody LocationRequestDto locationRequestDto,
                                                           BindingResult bindingResult,
                                                           HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            throw new LocationValidationException(bindingResult);
        }

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

}
