package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PostServiceTest extends IntegrationTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;



    @DisplayName("게시물 CRUD 테스트")
    @Nested
    class postLikeTest {

        Post post;
        User user;

//        @BeforeEach
//        void init() {
//            user = userRepository.saveAndFlush(User.builder()
//                    .email("user1@naver.com")
//                    .nickname("user1")
//                    .introduction("today is fun")
//                    .password("qwerqwer2@")
//                    .name("홍길동")
//                    .authority(COMMON)
//                    .job(Job.builder()
//                            .companyName("test company")
//                            .address("test address")
//                            .position("신입")
//                            .build())
//                    .build());
//
//            post = postRepository.saveAndFlush(Post.builder()
//                    .title("게시글 제목")
//                    .content("게시글 내용")
//                    .status(PostStatus.ACTIVE)
//                    .category("개발")
//                    .author("작성자")
//                    .build());
//        }


        @DisplayName("게시물이 등록된다.")
        @Test
        public void savePost() {
            // given
//            PostRequestDto.builder()
//                    .title("게시글 제목")
//                    .author("작성자")
//                    .content("내용")
//                    .files()
            // when

            // then
        }

        @DisplayName("게시물이 수정된다.")
        @Test
        public void updatePost() {
            // given

        }

        @DisplayName("게시물이 삭제된다.")
        @Test
        public void deletePost() {
            // given

        }


    }
}
