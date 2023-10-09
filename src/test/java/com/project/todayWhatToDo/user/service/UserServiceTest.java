package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.repository.UserRepository;
import com.project.todayWhatToDo.user.login.LoginApiManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class UserServiceTest {

    UserRepository userRepository;
    UserService userService;

    public UserServiceTest() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository, new LoginApiManager());
    }

    @DisplayName("로그인 진행을 위한 토큰을 발급 받으면 이메일과 비밀번호를 조회할 수 있다.")
    @Test
    public void loadUserByUsername() {
        //given
        var findUser = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .password("qwerqwer2@")
                .name("홍길동")
                .authority("USER")
                .build();

        given(userRepository.findByName(any()))
                .willReturn(Optional.of(findUser));
        //when
        var userDetail = userService.loadUserByUsername("홍길동");
        //then
        assertThat(userDetail.getUsername()).isEqualTo("today@naver.com");
        assertThat(userDetail.getPassword()).isEqualTo("qwerqwer2@");
    }

    @Test
    public void loadUserByUsernameWhenFail() {
        //given
        given(userRepository.findByName(any()))
                .willReturn(Optional.empty());
        //when //then
        assertThatThrownBy(() -> userService.loadUserByUsername("홍길동"))
                .hasMessage("홍길동" + " 이름을 가진 유저는 존재하지 않습니다.");
    }
}
