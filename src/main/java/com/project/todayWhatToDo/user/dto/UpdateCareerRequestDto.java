package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateCareerRequestDto(
        Long careerId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String introduction,
        String position
) {
}
