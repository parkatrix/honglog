package com.honglog.api.exception;

import lombok.Getter;

@Getter
public class Unauthorized extends HonglogException{

    private static final String MESSAGE = "인증이 필요합니다.";


    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }



}
