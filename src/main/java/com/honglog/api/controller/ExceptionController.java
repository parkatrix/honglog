package com.honglog.api.controller;

import com.honglog.api.exception.HonglogException;
import com.honglog.api.exception.InvalidRequest;
import com.honglog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

        Map<String, String> validation = new HashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            validation.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(BAD_REQUEST.value()))
                .message("잘못된 요청입니다.")
                .validation(validation)
                .build();

        return errorResponse;
    }

    @ResponseBody
    @ExceptionHandler(HonglogException.class)
    public ResponseEntity<ErrorResponse> postNotFoundHandler(HonglogException e) {

        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode).body(body);

    }


}
