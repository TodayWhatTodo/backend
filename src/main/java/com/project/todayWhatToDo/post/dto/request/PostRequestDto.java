package com.project.todayWhatToDo.post.dto.request;

import com.project.todayWhatToDo.post.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostRequestDto {

    private String author;
    private String title;
    private PostStatus postStatus;
    private String content;
    private String category;
    private Integer likeCount;


    public PostRequestDto toEntity() {
        return PostRequestDto.builder()
                .author(author)
                .title(title)
                .postStatus(postStatus)
                .content(content)
                .category(category)
                .likeCount(likeCount)
                .build();
    }

}
