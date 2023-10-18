package com.project.todayWhatToDo.post.dto;

import lombok.Builder;

@Builder
public record PostLikeRequestDto(
        Long postId,
        Long userId
) {
}
