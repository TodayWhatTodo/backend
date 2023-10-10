package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class UserIntegrationTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("user를 저장하면")
    class WhenSaveUser {

        User user;

        @BeforeEach
        public void saveUser() {
            //given
            user = userRepository.saveAndFlush(User.builder()
                    .email("today@naver.com")
                    .nickname("today")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .build());
        }

        @DisplayName("id 조회시 null이 아니다.")
        @Test
        public void getId() {
            //when //then
            assertThat(user.getId()).isNotNull();
        }

        @DisplayName("유저 가입한 시간을 조회할 수 있다. 오차 범위는 1초다")
        @Test
        public void getCreatedAt() {
            //when //then
            assertThat(user.getCreatedAt())
                    .isBetween(
                            LocalDateTime.now().minusSeconds(1),
                            LocalDateTime.now()
                    );
        }

        @DisplayName("유저 방문한 시간은 가입 시간과 동일하다.")
        @Test
        public void getVisitedAt() {
            //when //then
            assertThat(user.getCreatedAt())
                    .isEqualTo(user.getVisitedAt());
        }
    }

}
