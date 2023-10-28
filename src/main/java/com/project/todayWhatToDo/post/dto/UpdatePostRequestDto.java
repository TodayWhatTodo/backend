package com.project.todayWhatToDo.post.dto;

import com.project.todayWhatToDo.post.domain.PostStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatePostRequestDto(
        Long id,
        String author,
        String title,
        PostStatus status,
        String content,
        String category,
        List<String> keywordList

) {

}
