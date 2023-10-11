package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.security.Authority;
import com.project.todayWhatToDo.user.domain.Company;
import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.user.domain.Follow;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.*;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.login.LoginApiManager;
import com.project.todayWhatToDo.user.login.LoginApiProvider;
import com.project.todayWhatToDo.user.login.handler.LoginResponseHandler;
import com.project.todayWhatToDo.user.dto.*;
import com.project.todayWhatToDo.user.repository.FollowRepository;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static com.project.todayWhatToDo.security.Authority.COMMON;
import static org.assertj.core.api.Assertions.*;

@Transactional
@DisplayName("유저 서비스 테스트")
public class UserServiceTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    UserService userService;
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

            var request = ModifyUserRequestDto.builder()
                    .id(1L)
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
                    .build());

            var request = ModifyUserRequestDto.builder()
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
                    .company(Company.builder()
                            .name("hello company")
                            .address("test address")
                            .build())
                    .name("홍길동")
                    .imagePath("before image path")
                    .authority(Authority.COMMON)
                    .build());

            var request = ModifyUserRequestDto.builder()
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
                    .company(Company.builder()
                            .name("hello company")
                            .address("test address")
                            .build())
                    .name("홍길동")
                    .imagePath("before image path")
                    .authority(COMMON)
                    .build());

            var request = ModifyUserRequestDto.builder()
                    .companyName("bye company")
                    .build();
            //when
            userService.modifyUserInfo(request);
            //then
            assertThat(userRepository.findById(user.getId()))
                    .get()
                    .extracting("company.name")
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
                    .company(Company.builder()
                            .name("hello company")
                            .address("test address")
                            .build())
                    .name("홍길동")
                    .authority(COMMON)
                    .build());

            var request = ModifyUserRequestDto.builder().build();
            //when
            userService.modifyUserInfo(request);
            //then
            var findUser = userRepository.findById(user.getId()).orElseThrow();
            assertThat(findUser.getNickname()).isEqualTo("today");
            assertThat(findUser.getIntroduction()).isEqualTo("today is fun");
            assertThat(findUser.getCompany().getName()).isEqualTo("hello company");
        }
    }

    @Nested
    @DisplayName("경력 사항")
    class Career {
        @DisplayName("추가 : 회사경력을 추가하면 유저 커리어에 반영된다.")
        @Test
        public void createCareer() {
            //given
            var user = userRepository.saveAndFlush(User.builder()
                    .email("today@naver.com")
                    .nickname("today")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .build());

            var startedAt = LocalDateTime.of(2000, 10, 10, 10, 10, 10);
            var endedAt = LocalDateTime.of(2001, 10, 10, 10, 10, 10);
            var request = new CreateCareerRequestDto(user.getId(), "todo company", "test address", "my first job", startedAt, endedAt, "대리");
            //when
            userService.createCareer(request);
            //then
            assertThat(user.getCareers()).extracting("introduction", "startedAt", "endedAt", "position")
                    .contains(Tuple.tuple("my first job", startedAt, endedAt, "대리"));

        }
    }

    @Nested
    @DisplayName("팔로우")
    class FollowTest {

        User userA, userB, userC;


        @BeforeEach
        void init() {
            userB = userRepository.saveAndFlush(User.builder()
                    .email("user1@naver.com")
                    .nickname("user1")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .build());

            userA = userRepository.saveAndFlush(User.builder()
                    .email("user2@naver.com")
                    .nickname("user2")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .build());

            userC = userRepository.saveAndFlush(User.builder()
                    .email("user3@naver.com")
                    .nickname("user3")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .build());
        }

        @DisplayName("팔로우 : 팔로우 등록시 팔로잉 유저의 팔로잉 수가 1 증가하고 팔로워 유저의 팔로워 수가 1 증가한다.")
        @Test
        public void follow() {
            //given
            var request = FollowRequestDto.builder()
                    .followerId(userB.getId())
                    .userId(userA.getId())
                    .build();
            //when
            userService.follow(request);
            //then
            assertThat(userA.getFollowingCount()).isOne();
            assertThat(userB.getFollowerCount()).isOne();
        }

        @DisplayName("팔로우 취소시 팔로워 수와 팔로잉 수가 줄어든다.")
        @Test
        public void followCancel() {
            //given
            var follow = followRepository.saveAndFlush(Follow.builder()
                    .following(userA)
                    .follower(userB)
                    .build());

            var request = FollowCancelRequestDto.builder()
                    .followerId(userB.getId())
                    .followingId(userA.getId())
                    .build();
            //when
            userService.followCancel(request);
            //then
            assertThat(userB.getFollowerCount()).isEqualTo(-1);
            assertThat(userA.getFollowingCount()).isEqualTo(-1);
        }

        @DisplayName("팔로잉 목록을 조회한다.")
        @Test
        public void followingList() {
            //given
            var user = userA;
            follow(user, userB);
            follow(user, userC);

            var request = GetFollowingListRequestDto.builder()
                    .userId(user.getId())
                    .build();

            var pageable = PageRequest.of(0, 100);
            //when
            Page<FollowDto> list = userService.followingList(request, pageable);
            //then
            assertThat(list).extracting("followingId", "followerId", "followerNickname", "followingNickname")
                    .containsExactly(
                            Tuple.tuple(user.getId(), userB.getId(), userB.getNickname(), user.getNickname()),
                            Tuple.tuple(user.getId(), userC.getId(), userC.getNickname(), user.getNickname())
                    );
        }

        @DisplayName("팔로워 목록을 조회한다.")
        @Test
        public void followerList() {
            //given
            var follower = userB;

            follow(userA, follower);
            follow(userC, follower);

            var request = GetFollowerListRequestDto.builder()
                    .userId(follower.getId())
                    .build();

            var pageable = PageRequest.of(0, 100);
            //when
            Page<FollowDto> list = userService.followerList(request, pageable);
            //then
            assertThat(list).extracting("followingId", "followerId", "followerNickname", "followingNickname")
                    .containsExactly(
                            Tuple.tuple(userA.getId(), follower.getId(), follower.getNickname(), userA.getNickname()),
                            Tuple.tuple(userC.getId(), follower.getId(), follower.getNickname(), userC.getNickname())
                    );
        }

        @DisplayName("팔로잉이 없는 유저가 팔로잉 수를 조회할 때는 0을 반환한다")
        @Test
        public void countFollowing() {
            //given //when
            int count = userService.countFollowing(userA.getId());
            //then
            assertThat(count).isZero();
        }

        @DisplayName("팔로잉을 추가한 이후 팔로잉 수 증가가 반영되어 반환한다")
        @Test
        public void countFollowingWithFollow() {
            // given
            userService.follow(FollowRequestDto.builder()
                    .userId(userA.getId())
                    .followerId(userC.getId())
                    .build());
            // when
            int count = userService.countFollowing(userA.getId());
            // then
            assertThat(count).isOne();
        }

        @DisplayName("팔로잉이 없는 유저가 팔로잉 수를 조회할 때는 0을 반환한다")
        @Test
        public void countFollower() {
            //given //when
            int count = userService.countFollower(userA.getId());
            //then
            assertThat(count).isZero();
        }

        @DisplayName("팔로잉을 추가한 이후 팔로잉 수 증가가 반영되어 반환한다")
        @Test
        public void countFollowerWithFollow() {
            // given
            userService.follow(FollowRequestDto.builder()
                    .userId(userC.getId())
                    .followerId(userA.getId())
                    .build());
            // when
            int count = userService.countFollower(userA.getId());
            // then
            assertThat(count).isOne();
        }

        private void follow(User from, User to) {
            followRepository.saveAndFlush(Follow.builder()
                    .following(from)
                    .follower(to)
                    .build());
        }
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