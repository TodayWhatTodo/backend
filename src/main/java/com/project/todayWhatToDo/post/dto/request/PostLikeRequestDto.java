package com.project.todayWhatToDo.post.dto.request;

import lombok.Builder;

@Builder
public record PostLikeRequestDto(
        Long postId,
        Long userId
) {
}
