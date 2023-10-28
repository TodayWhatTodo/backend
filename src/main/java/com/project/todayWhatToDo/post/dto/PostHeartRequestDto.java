package com.project.todayWhatToDo.post.dto;

import lombok.Builder;

@Builder
public record PostHeartRequestDto(
        Long postId,
        Long userId
) {
}
