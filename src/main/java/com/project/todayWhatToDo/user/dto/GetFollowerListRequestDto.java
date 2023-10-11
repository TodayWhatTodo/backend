package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record GetFollowerListRequestDto(
        Long userId
) {
}
