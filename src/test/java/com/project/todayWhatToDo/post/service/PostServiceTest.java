package com.project.todayWhatToDo.post.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.post.domain.KeywordInfo;
import com.project.todayWhatToDo.post.domain.Post;
import com.project.todayWhatToDo.post.domain.PostStatus;
import com.project.todayWhatToDo.post.dto.PostRequestDto;
import com.project.todayWhatToDo.post.repository.PostRepository;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class PostServiceTest extends IntegrationTest {

    @Autowired
    PostService postService;
    @Autowired
    KeywordInfoService keywordInfoService;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @DisplayName("게시물 CRUD 테스트")
    @Nested
    class postLikeTest {

        PostRequestDto requestDto;

        @BeforeEach
        void init() {
             requestDto = PostRequestDto.builder()
                    .author("작성자")
                    .title("제목")
                    .status(PostStatus.ACTIVE)
                    .category("개발")
                    .content("내용")
                    .keywordList(Arrays.asList("태그33", "태그2"))
                    .build();
        }

        @DisplayName("게시물 등록.")
        @Nested
        class savePost {

            @DisplayName("게시물이 저장된다.")
            @Test
            void save_post() {
                // given
                // when
                Long savedId = postService.save(requestDto);
                // then
                Post findPost = postService.findById(savedId);
                assertThat(savedId).isEqualTo(findPost.getId());
            }

            @DisplayName("키워드가 저장된다.")
            @Test
            void save_keyword() {
                // given
                // when
                Long savedId = postService.save(requestDto);
                List<KeywordInfo> keywordInfoList = keywordInfoService.findByPostId(savedId);
                // then
                List<String> keywordList = keywordInfoList.stream().map(keywordInfo -> keywordInfo.getKeyword().getKeyword()).toList();
                assertThat(keywordList).isEqualTo(requestDto.keywordList());
            }
        }

        @DisplayName("게시물이 수정된다.")
        @Test
        public void updatePost() {
            // given
            requestDto = PostRequestDto.builder()
                    .keywordList(List.of("태그수정")).build();
            // when
            postService.update(requestDto);
            //then

        }

        @DisplayName("게시물이 삭제된다.")
        @Test
        public void deletePost() {
            // given

        }


    }
}
