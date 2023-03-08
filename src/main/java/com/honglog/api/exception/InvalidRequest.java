package com.honglog.api.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends HonglogException{

    private static final String MESSAGE = "잘못된 요청입니다.";
    private String fieldName;
    private String message;

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        super.addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }



}
