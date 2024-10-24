package com.craftelix.weatherviewer.controller;

import com.craftelix.weatherviewer.dto.SessionDto;
import com.craftelix.weatherviewer.dto.UserCreateDto;
import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.exception.InvalidPasswordException;
import com.craftelix.weatherviewer.exception.PasswordMismatchException;
import com.craftelix.weatherviewer.exception.UserAlreadyExistException;
import com.craftelix.weatherviewer.exception.UserNotFoundException;
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
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("user", new UserDto());
        return mv;
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("user") UserDto userDto, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        try {
            User user = authenticationService.authenticate(userDto);

            SessionDto session = sessionService.createSession(user.getId());

            Cookie cookie = buildSessionIdCookie(session);
            response.addCookie(cookie);

            mv.addObject("login", userDto.getLogin());
            mv.setViewName("redirect:/");
        } catch (UserNotFoundException | InvalidPasswordException e) {
            mv.addObject("errorMessage", e.getMessage());
            mv.setViewName("login");
        }
        return mv;
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView mv = new ModelAndView("register");
        mv.addObject("user", new UserCreateDto());
        return mv;
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("user") UserCreateDto userCreateDto) {
        ModelAndView mv = new ModelAndView();
        try {
            userService.save(userCreateDto);
            mv.addObject("successMessage", "User registered successfully. Go to sign in page.");
        } catch (UserAlreadyExistException | PasswordMismatchException e) {
            mv.addObject("errorMessage", e.getMessage());
        }
        mv.setViewName("register");
        return mv;
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
