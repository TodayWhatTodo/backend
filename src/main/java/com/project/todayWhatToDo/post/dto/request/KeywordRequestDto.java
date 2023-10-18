package com.project.todayWhatToDo.post.dto.request;

import lombok.Builder;

@Builder
public record KeywordRequestDto(
        Long postId,
        String keyword
) {

}
