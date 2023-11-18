package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.security.Authority;
import com.project.todayWhatToDo.user.domain.Job;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.request.CreateUserRequest;
import com.project.todayWhatToDo.user.dto.request.LoginRequest;
import com.project.todayWhatToDo.user.dto.request.ModifyUserRequest;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.login.LoginApiManager;
import com.project.todayWhatToDo.user.login.LoginApiProvider;
import com.project.todayWhatToDo.user.login.handler.LoginResponseHandler;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static com.project.todayWhatToDo.security.Authority.COMMON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Transactional
@DisplayName("유저 서비스 테스트")
public class UserServiceTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @MockBean
    LoginApiManager loginApiManager;

    @Test
    public void loadUserByUsernameWhenFail() {
        //given //when //then
        assertThatThrownBy(() -> userService.loadUserByUsername("홍길동"))
                .hasMessage("홍길동" + " 이름을 가진 유저는 존재하지 않습니다.");
    }

    @Nested
    @DisplayName("로그인")
    class Login {
        @DisplayName("로그인 진행을 위한 토큰을 발급 받으면 이메일과 비밀번호를 조회할 수 있다.")
        @Test
        public void loadUserByUsername() {
            //given
            var findUser = userRepository.save(User.builder()
                    .email("today@naver.com")
                    .nickname("today")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .job(Job.builder()
                            .companyName("test company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .build());

            //when
            var userDetail = userService.loadUserByUsername("홍길동");
            //then
            assertThat(userDetail.getUsername()).isEqualTo("today@naver.com");
            assertThat(userDetail.getPassword()).isEqualTo("qwerqwer2@");
        }
    }

    @Nested
    @DisplayName("프로필")
    class Profile {

        @DisplayName("수정 : 유저 닉네임을 수정하면 변경된다.")
        @Test
        public void updateNickname() {
            //given
            var user = userRepository.save(User.builder()
                    .email("today@naver.com")
                    .nickname("today")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .build());

            var request = ModifyUserRequest.builder()
                    .id(user.getId())
                    .nickname("after nickname")
                    .build();
            //when
            userService.modifyUserInfo(request);
            //then
            assertThat(userRepository.findById(user.getId()).orElseThrow()
                    .getNickname()).isEqualTo("after nickname");
        }

        @DisplayName("수정 : 소개글을 수정하면 변경된다.")
        @Test
        public void updateIntroduction() {
            //given
            var user = userRepository.saveAndFlush(User.builder()
                    .email("today@naver.com")
                    .nickname("today")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .job(Job.builder()
                            .companyName("test company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .build());

            var request = ModifyUserRequest.builder()
                    .id(user.getId())
                    .introduction("after self introduction")
                    .build();
            //when
            userService.modifyUserInfo(request);
            //then
            assertThat(userRepository.findById(user.getId()).orElseThrow()
                    .getIntroduction()).isEqualTo("after self introduction");
        }


        @DisplayName("이미지 경로 수정시 변경된다.")
        @Test
        public void updateImagePath() {
            //given
            var user = userRepository.saveAndFlush(User.builder()
                    .email("today@naver.com")
                    .nickname("today")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .job(Job.builder()
                            .companyName("hello company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .name("홍길동")
                    .imagePath("before image path")
                    .authority(Authority.COMMON)
                    .build());

            var request = ModifyUserRequest.builder()
                    .id(user.getId())
                    .imagePath("after image path")
                    .build();
            //when
            userService.modifyUserInfo(request);
            //then
            assertThat(user.getImagePath()).isEqualTo("after image path");
        }

        @DisplayName("수정 : 재직 중인 회사를 변경할 수 있다.")
        @Test
        public void updateCompanyName() {
            //given
            var user = userRepository.saveAndFlush(User.builder()
                    .email("today@naver.com")
                    .nickname("today")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .job(Job.builder()
                            .companyName("hello company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .name("홍길동")
                    .imagePath("before image path")
                    .authority(COMMON)
                    .build());

            var request = ModifyUserRequest.builder()
                    .id(user.getId())
                    .companyName("bye company")
                    .build();
            //when
            userService.modifyUserInfo(request);
            //then
            assertThat(userRepository.findById(user.getId()))
                    .get()
                    .extracting("job.companyName")
                    .isEqualTo("bye company");
        }

        @DisplayName("수정 : 유저 변경 정보가 null 이라면 수정 하지 않는다.")
        @Test
        public void doNotUpdate() {
            //given
            var user = userRepository.saveAndFlush(User.builder()
                    .email("today@naver.com")
                    .nickname("today")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .job(Job.builder()
                            .companyName("hello company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .name("홍길동")
                    .authority(COMMON)
                    .build());

            var request = ModifyUserRequest.builder()
                    .id(user.getId())
                    .build();
            //when
            userService.modifyUserInfo(request);
            //then
            var findUser = userRepository.findById(user.getId()).orElseThrow();
            assertThat(findUser.getNickname()).isEqualTo("today");
            assertThat(findUser.getIntroduction()).isEqualTo("today is fun");
            assertThat(findUser.getJob().getCompanyName()).isEqualTo("hello company");
        }
    }


    @DisplayName("로그인 : 로그인 성공시 예외가 발생하지 않는다")
    @Test
    public void loginSuccess() {
        //given
        mockingGetUserInfo("today@naver.com", "홍길동", "qwerqwer2@");

        userRepository.saveAndFlush(User.builder()
                .email("today@naver.com")
                .name("홍길동")
                .password("qwerqwer2@")
                .authority(COMMON)
                .job(Job.builder()
                        .companyName("test company")
                        .address("test address")
                        .position("신입")
                        .build())
                .build());

        var request = new LoginRequest("kakao", "test token");

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

        userRepository.saveAndFlush(User.builder()
                .email("today@naver.com")
                .name("홍길동")
                .password("qwerqwer2@")
                .authority(COMMON)
                .job(Job.builder()
                        .companyName("test company")
                        .address("test address")
                        .position("신입")
                        .build())
                .build());

        var request = new LoginRequest("kakao", "test token");
        //when //then
        assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(UserNotFoundException.class);
    }

    private void mockingGetUserInfo(String email, String name, String password) {
        var loginProvider = mock(LoginApiProvider.class);
        given(loginApiManager.getProvider(any()))
                .willReturn(loginProvider);

        var handler = mock(LoginResponseHandler.class);
        given(loginProvider.getUserInfo(any(), any()))
                .willReturn(handler);

        given(handler.getEmail()).willReturn(email);
        given(handler.getName()).willReturn(name);
        given(handler.getPassword()).willReturn(password);
    }

    @DisplayName("회원가입 : 회원가입 성공시 회원 정보를 반환한다.")
    @Test
    public void joinUser() {
        //given
        var request = CreateUserRequest.builder()
                .token("test token")
                .nickname("hello world")
                .companyName("hello company")
                .isAcceptAlarm(false)
                .build();

        mockingGetUserInfo("hello@naver.com", "홍길동", "qwerqwer2@");

        //when
        var session = userService.joinUser(request);
        //then
        assertThat(session).extracting("email", "name")
                .containsExactly("hello@naver.com", "홍길동");
    }

    @DisplayName("프로필 조회시 유저의 정보를 반환한다.")
    @Test
    public void getProfile() {
        //given
        var user = userRepository.saveAndFlush(User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .job(Job.builder()
                        .companyName("before company")
                        .address("test address")
                        .position("신입")
                        .build()
                )
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build());
        //when
        var profile = userService.getProfile(user.getId());
        //then
        assertThat(profile)
                .extracting("profileImagePath", "followerCount", "followingCount", "nickname",
                        "introduction", "position", "company")
                .containsExactly(user.getImagePath(), user.getFollowerCount(), user.getFollowingCount(), user.getNickname(),
                        user.getIntroduction(), user.getJob().getPosition(), user.getJob().getCompanyName());
    }
}