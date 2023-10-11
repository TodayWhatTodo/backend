package com.project.todayWhatToDo.user.dto;

public record LoginRequestDto(
        String provider,
        String token
) {
}