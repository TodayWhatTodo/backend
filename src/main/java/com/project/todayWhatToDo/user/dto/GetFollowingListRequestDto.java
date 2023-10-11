package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record GetFollowingListRequestDto(
        Long userId
) {
}
