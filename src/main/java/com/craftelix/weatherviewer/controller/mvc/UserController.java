package com.craftelix.weatherviewer.controller.mvc;

import com.craftelix.weatherviewer.dto.SessionDto;
import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.dto.UserLoginDto;
import com.craftelix.weatherviewer.dto.UserSignupDto;
import com.craftelix.weatherviewer.exception.UserAlreadyExistException;
import com.craftelix.weatherviewer.service.AuthService;
import com.craftelix.weatherviewer.service.SessionService;
import com.craftelix.weatherviewer.service.UserService;
import com.craftelix.weatherviewer.util.CookiesUtil;
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

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    private final UserService userService;

    private final SessionService sessionService;

    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("user", new UserLoginDto());
        return modelAndView;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserLoginDto userLoginDto,
                              HttpServletResponse response) {
        UserDto user = authService.authenticate(userLoginDto);
        SessionDto session = sessionService.createSession(user.getId());

        Cookie cookie  = CookiesUtil.createCookie(CookiesUtil.SESSION_ID, session.getId().toString(), session.getExpiresAt());
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/signup")
    public ModelAndView showSignupForm() {
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("user", new UserSignupDto());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView signup(@Valid @ModelAttribute("user") UserSignupDto userSignupDto,
                               BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("signup");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        try {
            userService.save(userSignupDto);
        } catch (UserAlreadyExistException e) {
            bindingResult.reject("userExists", e.getMessage());
            return modelAndView;
        }

        modelAndView.addObject("successMessage", "User registered successfully. Go to log in page.");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "sessionId", defaultValue = "") String sessionId,
                         HttpServletResponse response) {
        if (!sessionId.isBlank()) {
            sessionService.removeSession(UUID.fromString(sessionId));

            Cookie cookie  = CookiesUtil.createCookieToDelete(CookiesUtil.SESSION_ID);
            response.addCookie(cookie);
        }
        return "redirect:/login";
    }

}
