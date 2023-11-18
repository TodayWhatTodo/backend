package com.project.todayWhatToDo.user.dto.request;

public record LoginRequest(
        String provider,
        String token
) {
}