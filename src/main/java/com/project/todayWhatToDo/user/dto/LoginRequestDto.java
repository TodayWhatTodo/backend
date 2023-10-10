package com.project.todayWhatToDo.user.dto;

import lombok.Getter;

@Getter

public class LoginRequestDto {
    private String oauthProvider;
    private String token;
}
