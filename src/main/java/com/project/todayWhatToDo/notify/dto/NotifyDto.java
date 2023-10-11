package com.project.todayWhatToDo.notify.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotifyDto(
        Long id,
        LocalDateTime createdAt,
        String content
) {
}
