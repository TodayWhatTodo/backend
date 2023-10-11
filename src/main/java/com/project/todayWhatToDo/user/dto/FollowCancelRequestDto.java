package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record FollowCancelRequestDto(
        Long followerId,
        Long followingId
) {
}
