package com.craftelix.weatherviewer.controller;

import com.craftelix.weatherviewer.dto.openweather.LocationDto;
import com.craftelix.weatherviewer.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocationController {

    private final WeatherService weatherService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam(name = "q") String query) {
        List<LocationDto> locations = weatherService.findLocationsByName(query);
        ModelAndView mv = new ModelAndView("search");
        mv.addObject("locations", locations);
        return mv;
    }

}
