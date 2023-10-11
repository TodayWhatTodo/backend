package com.project.todayWhatToDo.user.dto;

import lombok.Builder;

@Builder
public record ProfileResponseDto(
        String profileImagePath,
        Integer followerCount,
        Integer followingCount,
        String nickname,
        String introduction,
        String position,
        String company
) {
}
