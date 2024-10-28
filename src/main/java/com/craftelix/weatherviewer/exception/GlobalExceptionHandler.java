package com.craftelix.weatherviewer.exception;

import com.craftelix.weatherviewer.dto.UserSignupDto;
import com.craftelix.weatherviewer.dto.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserAlreadyExistException.class, PasswordMismatchException.class})
    public ModelAndView handleRegisterExceptions(final Exception ex, final HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/register");
        modelAndView.addObject("user", new UserSignupDto(request.getParameter("login"), "", ""));
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({UserNotFoundException.class, InvalidPasswordException.class})
    public ModelAndView handleAuthenticationExceptions(final Exception ex, final HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("user", new UserLoginDto(request.getParameter("login"), ""));
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ModelAndView handleSessionNotFoundExceptions() {
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("user", new UserLoginDto());
        return modelAndView;
    }

}
