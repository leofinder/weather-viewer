package com.craftelix.weatherviewer.controller.mvc;

import com.craftelix.weatherviewer.dto.LocationWeatherDto;
import com.craftelix.weatherviewer.dto.LocationWithUserStatusDto;
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
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        List<LocationWeatherDto> locations = locationService.getUserLocationsWithWeatherData(user);

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", user);
        modelAndView.addObject("locations", locations);
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam(name = "name") String name,
                               HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        List<LocationApiDto> locationsApiDto = weatherService.findLocationsByName(name);
        List<LocationWithUserStatusDto> locationsWithUserStatus = locationService.setUserStatusForLocations(locationsApiDto, user);

        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("name", name);
        modelAndView.addObject("user", user);
        modelAndView.addObject("locations", locationsWithUserStatus);
        return modelAndView;
    }

}
