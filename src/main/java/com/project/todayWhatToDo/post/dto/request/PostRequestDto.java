package com.project.todayWhatToDo.post.dto.request;

import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.domain.PostStatus;
import com.project.todayWhatToDo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostRequestDto {

    private Long id;
    private String author;
    private String title;
    private PostStatus status;
    private String content;
    private String category;
    private Integer like;
    // 좋아요 누른 사람 정보
    private User user;


    public Post toEntity() {
        return Post.builder()
                .author(author)
                .title(title)
                .status(status)
                .content(content)
                .category(category)
                .like(like)
                .build();
    }

}
