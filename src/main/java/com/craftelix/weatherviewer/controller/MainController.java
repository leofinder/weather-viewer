package com.craftelix.weatherviewer.controller;

import com.craftelix.weatherviewer.dto.api.LocationDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.service.LocationService;
import com.craftelix.weatherviewer.service.UserService;
import com.craftelix.weatherviewer.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final WeatherService weatherService;

    private final UserService userService;

    private final LocationService locationService;

    @GetMapping("/")
    public ModelAndView index(@CookieValue(value = "sessionId", defaultValue = "") String sessionId) {
        User user = userService.getUserBySessionId(UUID.fromString(sessionId));
        List<LocationDto> locations = locationService.findLocationsByUser(user);

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("login", user.getLogin());
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam(name = "name") String name,
                               @CookieValue(value = "sessionId", defaultValue = "") String sessionId) {
        User user = userService.getUserBySessionId(UUID.fromString(sessionId));
        List<LocationDto> locations = weatherService.findLocationsByName(name);
        locations = locationService.markLocationsAsAddedForUser(locations, user);

        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("login", user.getLogin());
        modelAndView.addObject("locations", locations);
        return modelAndView;
    }

}
