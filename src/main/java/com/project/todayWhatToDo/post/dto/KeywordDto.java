package com.project.todayWhatToDo.post.dto;

import com.project.todayWhatToDo.post.domain.Keyword;
import lombok.Builder;

@Builder
public record KeywordDto(
        Long keywordId,
        String keyword
) {

    public Keyword toEntity() {
        return Keyword.builder()
                .keyword(keyword)
                .build();
    }
}
