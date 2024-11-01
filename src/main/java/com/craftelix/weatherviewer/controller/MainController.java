package com.craftelix.weatherviewer.controller;

import com.craftelix.weatherviewer.dto.LocationResponseDto;
import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.service.LocationService;
import com.craftelix.weatherviewer.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final WeatherService weatherService;

    private final LocationService locationService;

    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        UserDto user = (UserDto) request.getAttribute("user");
        List<LocationResponseDto> locations = locationService.getUserLocationsWithWeatherData(user);

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", user);
        modelAndView.addObject("locations", locations);
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam(name = "name") String name,
                               HttpServletRequest request) {
        UserDto user = (UserDto) request.getAttribute("user");
        List<LocationApiDto> locations = weatherService.findLocationsByName(name);
        locations = locationService.markLocationsAsAddedForUser(locations, user);

        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("user", user);
        modelAndView.addObject("locations", locations);
        return modelAndView;
    }

}
