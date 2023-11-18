package com.project.todayWhatToDo.user.dto.request;

import java.time.LocalDateTime;

public record CreateCareerRequest(
        Long userId,
        String name,
        String address,
        String introduction,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String position
) {

}
