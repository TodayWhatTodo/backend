package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.domain.PostStatus;
import com.project.todayWhatToDo.post.dto.KeywordRequestDto;
import com.project.todayWhatToDo.post.dto.PostRequestDto;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

//@Transactional
public class PostServiceTest extends IntegrationTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @DisplayName("게시물 CRUD 테스트")
    @Nested
    class postTest {

        PostRequestDto requestDto;

        @BeforeEach
        void init() {
             requestDto = PostRequestDto.builder()
                    .author("작성자")
                    .title("제목")
                    .status(PostStatus.ACTIVE)
                    .category("개발")
                    .content("내용")
                    .keywordList(Arrays.asList(KeywordRequestDto.builder().keyword("키워드21").build(),
                            KeywordRequestDto.builder().keyword("키워드2").build()))
                    .build();
        }

        @AfterEach
        void tearDown() {
            postRepository.deleteAll();
        }


        @DisplayName("게시물이 저장된다.")
        @Test
        void save_post() {
            // given
            // when
            Long savedId = postService.save(requestDto);
            // then
            Post findPost = postService.findFetchById(savedId);
            assertThat(savedId).isEqualTo(findPost.getId());
        }



        @DisplayName("게시물이 수정된다. : 키워드 수정")
        @Test
        public void updatePost() {
            // given
//            requestDto = PostRequestDto.builder()
//                    .keywordList(KeywordRequestDto.builder()
//                            .keyword().build();
            // when
//            postService.update(requestDto);
            //then

        }

        @DisplayName("게시물이 삭제된다.")
        @Test
        public void deletePost() {
            // given

        }


    }
}
