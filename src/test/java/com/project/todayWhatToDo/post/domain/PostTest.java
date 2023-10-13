package com.project.todayWhatToDo.post.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("게시물 도메인 테스트")
public class PostTest {


    @DisplayName("getter method test")
    @Test
    void getter() {
        // given
        var post = Post.builder()
                .author("작성자")
                .content("내용")
                .like(20)
                .category("개발")
                .status(PostStatus.ACTIVE)
                .title("제목")
                .title("제목")
                .build();

        // when then
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getLike()).isEqualTo(20);
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getAuthor()).isEqualTo("작성자");
        assertThat(post.getCategory()).isEqualTo(PostStatus.ACTIVE);
        assertThat(post.getTitle()).isEqualTo("제목");

    }
    @Test
    @DisplayName("게시물 작성 테스트")
    public void savePost() {

    }
}
