package com.project.todayWhatToDo.user.login.handler;

import java.util.Map;

public abstract class LoginResponseHandler {
    private final Map<String, Object> response;

    public LoginResponseHandler(Map<String, Object> response) {
        this.response = response;
    }

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getPassword();
}
