package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.security.Authority;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.ModifyUserRequest;
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
                .authority(Authority.COMMON)
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

    @DisplayName("유저 닉네임을 수정하면 변경된다.")
    @Test
    public void updateNickname() {
        //given
        var findUser = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(findUser));

        var request = new ModifyUserRequest(1L, "after nickname", null);
        //when
        userService.modifyUserInfo(request);
        //then
        assertThat(findUser.getNickname()).isEqualTo("after nickname");
    }

    @DisplayName("")
    @Test
    public void updateIntroduction() {
        //given
        var findUser = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(findUser));

        var request = new ModifyUserRequest(1L, null, "after self introduction");
        //when
        userService.modifyUserInfo(request);
        //then
        assertThat(findUser.getIntroduction()).isEqualTo("after self introduction");
    }

    @DisplayName("유저 변경 정보가 null 이라면 수정 하지 않는다.")
    @Test
    public void dontUpdate() {
        //given
        var findUser = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(findUser));

        var request = new ModifyUserRequest(1L, null, null);
        //when
        userService.modifyUserInfo(request);
        //then
        assertThat(findUser.getNickname()).isEqualTo("today");
        assertThat(findUser.getIntroduction()).isEqualTo("today is fun");
    }
}