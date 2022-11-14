package com.honglog.api.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private List<ValidationTuple> validation = new ArrayList<>();

    public void addValidation(String fieldName, String errorMessage) {
        validation.add(new ValidationTuple(fieldName, errorMessage));
    }

    @RequiredArgsConstructor
    private class ValidationTuple {
        private final String fieldName;
        private final String errorMessage;
    }

}
