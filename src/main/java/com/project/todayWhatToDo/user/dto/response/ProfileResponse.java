package com.project.todayWhatToDo.user.dto.response;

public record ProfileResponse(
        String profileImagePath,
        Integer followerCount,
        Integer followingCount,
        String nickname,
        String introduction,
        String position,
        String company,
        Long userId
) {
}
