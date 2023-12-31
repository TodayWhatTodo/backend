package com.project.todayWhatToDo.user.login.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Map;

public abstract class LoginResponseHandler {
    @Getter
    private final Map<String, Object> response;

    public LoginResponseHandler(String response) {
        try {
            var om = new ObjectMapper();
            this.response = om.readValue(response, Map.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("로그인 API 응답 결과가 올바르지 않습니다");
        }
    }

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getPassword();
}
