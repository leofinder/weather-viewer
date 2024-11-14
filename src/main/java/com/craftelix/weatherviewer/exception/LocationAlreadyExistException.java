package com.craftelix.weatherviewer.exception;

public class LocationAlreadyExistException extends RuntimeException {

    public LocationAlreadyExistException(String message) {
        super(message);
    }
}
