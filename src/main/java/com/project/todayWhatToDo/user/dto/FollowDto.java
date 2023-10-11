package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record FollowDto(
        Long followerId,
        Long followingId,
        String followerNickname,
        String followingNickname
) {
}
