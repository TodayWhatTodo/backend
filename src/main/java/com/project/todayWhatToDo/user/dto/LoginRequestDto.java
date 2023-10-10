package com.project.todayWhatToDo.user.dto;

public record LoginRequestDto(
        String oauthProvider,
        String token
) {
}