package com.project.todayWhatToDo.user.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateCareerRequest(
        Long careerId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String introduction,
        String position
) {
}
