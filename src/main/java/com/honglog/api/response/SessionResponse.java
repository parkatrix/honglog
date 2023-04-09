package com.honglog.api.response;

import lombok.Data;
import lombok.Getter;

@Getter
public class SessionResponse {

    private final String accessToken;

    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
