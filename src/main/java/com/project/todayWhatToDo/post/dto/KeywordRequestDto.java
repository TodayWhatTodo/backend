package com.project.todayWhatToDo.post.dto;

import lombok.Builder;

@Builder
public record KeywordRequestDto(
        String keyword,
        String after

) {

}
