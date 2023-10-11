package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record UserSession(
        String email,
        String name,
        Long id
) {
}
