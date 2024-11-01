package com.craftelix.weatherviewer.controller;

import com.craftelix.weatherviewer.dto.SessionDto;
import com.craftelix.weatherviewer.dto.UserSignupDto;
import com.craftelix.weatherviewer.dto.UserLoginDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.UserValidationException;
import com.craftelix.weatherviewer.service.AuthenticationService;
import com.craftelix.weatherviewer.service.SessionService;
import com.craftelix.weatherviewer.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
        modelAndView.addObject("user", new UserLoginDto());
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("user") UserLoginDto userLoginDto,
                              HttpServletResponse response) {
        User user = authenticationService.authenticate(userLoginDto);
        SessionDto session = sessionService.createSession(user.getId());

        Cookie cookie = buildSessionIdCookie(session);
        response.addCookie(cookie);

        ModelAndView modelAndView = new ModelAndView("redirect:/");
        modelAndView.addObject("user", userLoginDto);
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView showSignupForm() {
        ModelAndView modelAndView = new ModelAndView("/signup");
        modelAndView.addObject("user", new UserSignupDto());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView signup(@Valid @ModelAttribute("user") UserSignupDto userSignupDto,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new UserValidationException(bindingResult);
        }

        userService.save(userSignupDto);

        ModelAndView modelAndView = new ModelAndView("/signup");
        modelAndView.addObject("successMessage", "User registered successfully. Go to sign in page.");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "sessionId", defaultValue = "") String sessionId,
                         HttpServletResponse response) {
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
