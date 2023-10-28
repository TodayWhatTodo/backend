package com.project.todayWhatToDo.post.dto;

import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.domain.PostStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record CreatePostRequestDto(
        Long id,
        String author,
        String title,
        PostStatus status,
        String content,
        String category,
        Integer likeCount,
        List<String> keywordList
) {

    public Post toEntity() {
        return Post.builder()
                .author(author)
                .title(title)
                .status(status)
                .content(content)
                .category(category)
                .keywords(keywordList)
                .build();
    }

}
