package com.project.todayWhatToDo.post.dto;

import lombok.Builder;

@Builder
public record CreateHeartRequest(
        Long postId,
        Long userId
) {
}
