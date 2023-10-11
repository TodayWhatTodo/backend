package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.user.domain.Follow;
import com.project.todayWhatToDo.user.domain.User;
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

            var request = new ModifyUserRequestDto(user.getId(), "after nickname", null, null);
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

            var request = new ModifyUserRequestDto(user.getId(), null, "after self introduction", null);
            //when
            userService.modifyUserInfo(request);
            //then
            assertThat(userRepository.findById(user.getId()).orElseThrow()
                    .getIntroduction()).isEqualTo("after self introduction");
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
                    .companyName("hello company")
                    .name("홍길동")
                    .authority(COMMON)
                    .build());

            var request = new ModifyUserRequestDto(user.getId(), null, null, "bye company");
            //when
            userService.modifyUserInfo(request);
            //then
            assertThat(userRepository.findById(user.getId()).orElseThrow()
                    .getCompanyName()).isEqualTo("bye company");
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
                    .companyName("hello company")
                    .name("홍길동")
                    .authority(COMMON)
                    .build());

            var request = new ModifyUserRequestDto(user.getId(), null, null, null);
            //when
            userService.modifyUserInfo(request);
            //then
            var findUser = userRepository.findById(user.getId()).orElseThrow();
            assertThat(findUser.getNickname()).isEqualTo("today");
            assertThat(findUser.getIntroduction()).isEqualTo("today is fun");
            assertThat(findUser.getCompanyName()).isEqualTo("hello company");
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
            var request = new CreateCareerRequestDto(user.getId(), "todo company", "my first job", startedAt, endedAt, "대리");
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
}