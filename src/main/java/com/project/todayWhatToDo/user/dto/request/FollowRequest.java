package com.project.todayWhatToDo.user.dto.request;

import lombok.Builder;

@Builder
public record FollowRequest(
        Long followerId,
        Long userId
) {
}
