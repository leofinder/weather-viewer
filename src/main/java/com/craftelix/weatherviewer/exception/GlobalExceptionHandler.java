package com.craftelix.weatherviewer.exception;

import com.craftelix.weatherviewer.dto.user.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
@Order(2)
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, InvalidPasswordException.class})
    public ModelAndView handleAuthenticationExceptions(final Exception ex, final HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("user", new UserLoginDto(request.getParameter("username"), ""));
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ModelAndView handleSessionNotFoundExceptions() {
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("user", new UserLoginDto());
        return modelAndView;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundExceptions(final Exception ex) {
        log.error("HTTP Response Code: {}. {}", HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return "/errors/404";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeExceptions(final Exception ex) {
        log.error("HTTP Response Code: {}. {}", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return "/errors/500";
    }

}
