package com.project.todayWhatToDo.user.request;

import lombok.Getter;

@Getter

public class LoginRequest {
    private String oauthProvider;
    private String token;
}
