package com.craftelix.weatherviewer.exception;

import com.craftelix.weatherviewer.dto.UserSignupDto;
import com.craftelix.weatherviewer.dto.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @ExceptionHandler(UserValidationException.class)
    public ModelAndView handleUserValidationException(final UserValidationException ex, final HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/register");
        modelAndView.addObject("user", new UserSignupDto(request.getParameter("login"), "", ""));
        modelAndView.addObject("errorMessage", buildErrorMessage(ex.getBindingResult(), "login", "password"));
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

    private String buildErrorMessage(BindingResult bindingResult, String... fieldsToCheck) {
        String errors = "";
        for (String field : fieldsToCheck) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(field);
            if (!fieldErrors.isEmpty()) {
                errors = fieldErrors.stream()
                        .map(FieldError::getDefaultMessage)
                        .filter(Objects::nonNull)
                        .sorted()
                        .collect(Collectors.joining(System.lineSeparator()));
                break;
            }
        }
        return errors;
    }
}
