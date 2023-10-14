package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.domain.PostStatus;
import com.project.todayWhatToDo.post.dto.request.PostLikeRequestDto;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.user.domain.Job;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.project.todayWhatToDo.security.Authority.COMMON;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class PostLikeServiceTest extends IntegrationTest {

    @Autowired
    PostService postService;
    @Autowired
    PostLikeService postLikeService;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @DisplayName("게시물 좋아요 테스트")
    @Nested
    class postLikeTest {

        Post post;
        User user;

        @BeforeEach
        void init() {
            user = userRepository.saveAndFlush(User.builder()
                    .email("user1@naver.com")
                    .nickname("user1")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .job(Job.builder()
                            .companyName("test company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .build());

            post = postRepository.saveAndFlush(Post.builder()
                    .title("게시글 제목")
                    .content("게시글 내용")
                    .status(PostStatus.ACTIVE)
                    .category("개발")
                    .author("작성자")
                    .build());
        }


        @DisplayName("게시물 좋아요 누르면 좋아요가 1이된다.")
        @Test
        public void likePost() {
            // given
            var requestDto = PostLikeRequestDto.builder()
                    .userId(user.getId())
                    .postId(post.getId())
                    .build();
            // when
            postLikeService.likePost(requestDto);
            // then
            Post found = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
            assertThat(found.getLike()).isOne();
        }

        @DisplayName("게시물 좋아요 취소시 -1이 된다.")
        @Test
        public void dislikePost() {
            // given

            // when

            // then
        }


    }
}
