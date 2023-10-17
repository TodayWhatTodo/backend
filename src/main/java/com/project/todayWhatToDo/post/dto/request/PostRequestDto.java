package com.project.todayWhatToDo.post.dto.request;

import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.domain.PostStatus;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Builder
public record PostRequestDto (
        Long id,
        String author,
        String title,
        PostStatus status,
        String content,
        String category,
        Integer likeCount,
        List<MultipartFile> files
) {

    public Post toEntity() {
        return Post.builder()
                .author(author)
                .title(title)
                .status(status)
                .content(content)
                .category(category)
                .build();
    }

}
