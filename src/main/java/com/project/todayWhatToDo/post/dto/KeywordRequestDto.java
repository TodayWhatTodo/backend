package com.project.todayWhatToDo.post.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record KeywordRequestDto(
        String keyword,
        String after

) {
    public KeywordRequestDto {
        keyword = Objects.requireNonNullElse(keyword, "");
        after = Objects.requireNonNullElse(after, "");
    }
}
