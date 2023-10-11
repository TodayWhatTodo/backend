package com.project.todayWhatToDo.notify.dto;

import lombok.Builder;

@Builder
public record GetNotifyRequestDto(
        Long userId
) {
}
