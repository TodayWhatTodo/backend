package com.project.todayWhatToDo.user.dto.response;

import lombok.Builder;

@Builder
public record ProfileResponse(
        String profileImagePath,
        Integer followerCount,
        Integer followingCount,
        String nickname,
        String introduction,
        String position,
        String company
) {
}
