package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record FollowRequestDto(
        Long followerId,
        Long userId
) {
}
