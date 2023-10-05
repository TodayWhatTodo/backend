package com.project.todayWhatToDo.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @DisplayName("getter method test")
    @Test
    void getter(){
        // given
        User user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .password("qwerqwer2@")
                .name("홍길동")
                .build();

        // when then
        assertThat(user.getName()).isEqualTo("홍길동");
        assertThat(user.getPassword()).isEqualTo("qwerqwer2@");
        assertThat(user.getNickname()).isEqualTo("today");
        assertThat(user.getEmail()).isEqualTo("today@naver.com");
    }
}
