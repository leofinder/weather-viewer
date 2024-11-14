package com.craftelix.weatherviewer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

@Getter
@RequiredArgsConstructor
public class LocationValidationException extends RuntimeException {

    private final BindingResult bindingResult;

}

