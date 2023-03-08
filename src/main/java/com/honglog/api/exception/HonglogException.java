package com.honglog.api.exception;

import lombok.Getter;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HonglogException extends RuntimeException {


    private final Map<String, String> validation = new HashMap<>();


    public HonglogException(String message) {
        super(message);
    }

    public HonglogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
