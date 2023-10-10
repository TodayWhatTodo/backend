package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.security.Authority;
import com.project.todayWhatToDo.user.domain.Company;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.*;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.login.LoginApiManager;
import com.project.todayWhatToDo.user.login.LoginApiProvider;
import com.project.todayWhatToDo.user.login.handler.LoginResponseHandler;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class UserServiceTest {

    UserRepository userRepository;
    UserService userService;
    LoginApiManager loginApiManager;

    public UserServiceTest() {
        userRepository = mock(UserRepository.class);
        loginApiManager = mock(LoginApiManager.class);
        userService = new UserService(userRepository, loginApiManager);
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

    @DisplayName("프로필 수정 : 유저 닉네임을 수정하면 변경된다.")
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

        var request = ModifyUserRequestDto.builder()
                .id(1L)
                .nickname("after nickname")
                .build();
        //when
        userService.modifyUserInfo(request);
        //then
        assertThat(findUser.getNickname()).isEqualTo("after nickname");
    }

    @DisplayName("프로필 수정 : 소개글을 수정하면 변경된다.")
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

        var request = ModifyUserRequestDto.builder()
                .id(1L)
                .introduction("after self introduction")
                .build();
        //when
        userService.modifyUserInfo(request);
        //then
        assertThat(findUser.getIntroduction()).isEqualTo("after self introduction");
    }

    @DisplayName("프로필 수정 : 재직 중인 회사를 변경할 수 있다.")
    @Test
    public void updateCompanyName() {
        //given
        var user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .password("qwerqwer2@")
                .company(Company.builder()
                        .name("hello company")
                        .address("test address")
                        .build())
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(user));

        var request = ModifyUserRequestDto.builder()
                .id(1L)
                .companyName("bye company")
                .build();
        //when
        userService.modifyUserInfo(request);
        //then
        assertThat(user.getCompany()).extracting("name").isEqualTo("bye company");
    }

    @DisplayName("프로필 수정 : 유저 변경 정보가 null 이라면 수정 하지 않는다.")
    @Test
    public void dontUpdate() {
        //given
        var findUser = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .password("qwerqwer2@")
                .company(Company.builder()
                        .name("hello company")
                        .address("test address")
                        .build())
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(findUser));

        var request = ModifyUserRequestDto.builder()
                .id(1L)
                .build();
        //when
        userService.modifyUserInfo(request);
        //then
        assertThat(findUser.getNickname()).isEqualTo("today");
        assertThat(findUser.getIntroduction()).isEqualTo("today is fun");
        assertThat(findUser.getCompany().getName()).isEqualTo("hello company");
    }

    @DisplayName("경력 추가 : 회사경력을 추가하면 유저 커리어에 반영된다.")
    @Test
    public void createCareer() {
        //given
        var user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(user));

        var startedAt = LocalDateTime.of(2000, 10, 10, 10, 10, 10);
        var endedAt = LocalDateTime.of(2001, 10, 10, 10, 10, 10);
        var request = new CreateCareerRequestDto(1L, "todo company", "test address", "my first job", startedAt, endedAt, "대리");
        //when
        userService.createCareer(request);
        //then
        assertThat(user.getCareers()).extracting("introduction", "startedAt", "endedAt", "position")
                .contains(Tuple.tuple("my first job", startedAt, endedAt, "대리"));

    }

    @DisplayName("로그인 : 로그인 성공시 예외가 발생하지 않는다")
    @Test
    public void loginSuccess() {
        //given
        mockingGetUserInfo("today@naver.com", "홍길동", "qwerqwer2@");
        var request = new LoginRequestDto("kakao", "test token");

        given(userRepository
                .findByEmailAndNameAndPassword("today@naver.com", "홍길동", "qwerqwer2@"))
                .willReturn(Optional.of(User.builder()
                        .email("today@naver.com")
                        .name("홍길동")
                        .build()));
        //when //then
        var session = userService.login(request);
        assertThat(session).extracting("email", "name")
                .containsExactly("today@naver.com", "홍길동");
    }

    @DisplayName("로그인 : 로그인 실패시 UserNotFoundException")
    @Test
    public void loginFail() {
        //given
        mockingGetUserInfo("today@naver.com", "홍길", "qwerqwer2@");
        var request = new LoginRequestDto("kakao", "test token");

        given(userRepository
                .findByEmailAndNameAndPassword("today@naver.com", "홍길동", "qwerqwer2@"))
                .willReturn(Optional.of(User.builder().build()));
        //when //then
        assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(UserNotFoundException.class);
    }

    private void mockingGetUserInfo(String email, String name, String password) {
        var loginProvider = mock(LoginApiProvider.class);

        given(loginApiManager.getProvider(any()))
                .willReturn(loginProvider);

        var handler = mock(LoginResponseHandler.class);
        given(loginProvider.getUserInfo(any()))
                .willReturn(handler);

        given(handler.getEmail()).willReturn(email);
        given(handler.getName()).willReturn(name);
        given(handler.getPassword()).willReturn(password);
    }

    @DisplayName("회원가입 : 회원가입 성공시 회원 정보를 반환한다.")
    @Test
    public void joinUser() {
        //given
        var request = CreateUserRequestDto.builder()
                .token("test token")
                .nickname("hello world")
                .companyName("hello company")
                .isAcceptAlarm(false)
                .build();

        mockingGetUserInfo("hello@naver.com", "홍길동", "qwerqwer2@");

        given(userRepository.save(any()))
                .willAnswer((mock) -> mock.getArgument(0));

        //when
        var session = userService.joinUser(request);
        //then
        assertThat(session).extracting("email", "name")
                .containsExactly("hello@naver.com", "홍길동");
    }
}