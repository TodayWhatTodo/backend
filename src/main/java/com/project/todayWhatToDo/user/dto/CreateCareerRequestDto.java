package com.project.todayWhatToDo.user.dto;

import java.time.LocalDateTime;

public record CreateCareerRequestDto(
        Long userId,
        String name,
        String introduction,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String position
) {

}
