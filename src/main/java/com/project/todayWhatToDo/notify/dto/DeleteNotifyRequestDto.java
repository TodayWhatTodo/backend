package com.project.todayWhatToDo.notify.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DeleteNotifyRequestDto(
        Long userId,
        List<Long> notifyIds
) {
}
