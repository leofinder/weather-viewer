package com.craftelix.weatherviewer.controller;

import com.craftelix.weatherviewer.dto.SessionDto;
import com.craftelix.weatherviewer.dto.UserCreateDto;
import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.service.AuthenticationService;
import com.craftelix.weatherviewer.service.SessionService;
import com.craftelix.weatherviewer.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final SessionService sessionService;

    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("user", new UserDto());
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("user") UserDto userDto, HttpServletResponse response) {
        User user = authenticationService.authenticate(userDto);
        SessionDto session = sessionService.createSession(user.getId());

        Cookie cookie = buildSessionIdCookie(session);
        response.addCookie(cookie);

        ModelAndView modelAndView = new ModelAndView("redirect:/");
        modelAndView.addObject("login", userDto.getLogin());
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", new UserCreateDto());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("user") UserCreateDto userCreateDto) {
        userService.save(userCreateDto);

        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("successMessage", "User registered successfully. Go to sign in page.");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "sessionId", defaultValue = "") String sessionId, HttpServletResponse response) {
        if (!sessionId.isBlank()) {
            sessionService.removeSession(UUID.fromString(sessionId));
            // TODO
            Cookie sessionIdCookie = new Cookie("sessionId", "");
            sessionIdCookie.setMaxAge(0);
            response.addCookie(sessionIdCookie);
        }
        return "redirect:/login";
    }

    private Cookie buildSessionIdCookie(SessionDto session) {
        Cookie cookie = new Cookie("sessionId", session.getId().toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        long maxAge = Duration.between(LocalDateTime.now(), session.getExpiresAt()).getSeconds();
        cookie.setMaxAge((int) maxAge);

        return cookie;
    }

}
