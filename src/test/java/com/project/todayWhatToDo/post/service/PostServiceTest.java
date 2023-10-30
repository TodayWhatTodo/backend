package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.domain.PostStatus;
import com.project.todayWhatToDo.post.dto.CreatePostRequest;
import com.project.todayWhatToDo.post.dto.UpdatePostRequest;
import com.project.todayWhatToDo.post.exception.PostNotFoundException;
import com.project.todayWhatToDo.post.repository.KeywordRepository;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class PostServiceTest extends IntegrationTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    KeywordRepository keywordRepository;

    @DisplayName("게시물 CRUD 테스트")
    @Nested
    class postTest {

        CreatePostRequest requestDto;

        @BeforeEach
        void init() {
             requestDto = CreatePostRequest.builder()
                    .author("작성자")
                    .title("제목")
                    .status(PostStatus.ACTIVE)
                    .category("개발")
                    .content("내용")
                    .keywordList(List.of("키워드1", "키워드2"))
                    .build();
        }

//        @AfterEach
//        void tearDown() {
//            postRepository.deleteAll();
//        }


        @DisplayName("게시물이 저장된다. 키워드도 저장된다.")
        @Test
        void save_post() {
            // given
            // when
            Long savedId = postService.save(requestDto);
            // then
            Post findPost = postRepository.findFetchById(savedId).orElseThrow(PostNotFoundException::new);
            assertThat(savedId).isEqualTo(findPost.getId());
        }



        @DisplayName("게시물이 수정된다. : 키워드 수정")
        @Test
        public void updatePost() {
            // given
            Long savedId = postService.save(requestDto);
            Post findPost = postRepository.findFetchById(savedId).orElseThrow(PostNotFoundException::new);
            UpdatePostRequest updateRequest = UpdatePostRequest.builder()
                    .id(savedId)
                    .content("수정된 내용")
                    .keywordList(List.of("수정된 키워드1", "수정된 키워드2")).build();
            // when
            postService.update(updateRequest);
            //then
            Post updatedPost = postRepository.findFetchById(savedId).orElseThrow(PostNotFoundException::new);
            assertThat(updatedPost.getKeywords())
                    .extracting("keyword")
                    .contains("수정된 키워드1", "수정된 키워드2");
            assertThat(updatedPost.getContent()).isEqualTo("수정된 내용");

        }

        @DisplayName("게시물이 삭제된다.")
        @Test
        public void deletePost() {
            // given

        }


    }
}
