package com.craftelix.weatherviewer.exception;

import com.craftelix.weatherviewer.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(1)
public class GlobalRestExceptionHandler {

    @ExceptionHandler(LocationValidationException.class)
    public ResponseEntity<Map<String, List<String>>> handleLocationValidationException(LocationValidationException ex) {
        List<String> errors = ValidationUtils.extractErrorMessages(ex.getBindingResult());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", errors));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
      return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(LocationAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleLocationAlreadyExistException(LocationAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }
}

