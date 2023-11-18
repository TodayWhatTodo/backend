package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.user.domain.Follow;
import com.project.todayWhatToDo.user.domain.Job;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.*;
import com.project.todayWhatToDo.user.dto.request.FollowCancelRequest;
import com.project.todayWhatToDo.user.dto.request.FollowRequest;
import com.project.todayWhatToDo.user.dto.request.GetFollowerListRequest;
import com.project.todayWhatToDo.user.dto.request.GetFollowingListRequest;
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

import static com.project.todayWhatToDo.security.Authority.COMMON;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class FollowServiceTest extends IntegrationTest {

    @Autowired
    FollowService followService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowRepository followRepository;

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
                    .job(Job.builder()
                            .companyName("test company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .build());

            userA = userRepository.saveAndFlush(User.builder()
                    .email("user2@naver.com")
                    .nickname("user2")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .job(Job.builder()
                            .companyName("test company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .build());

            userC = userRepository.saveAndFlush(User.builder()
                    .email("user3@naver.com")
                    .nickname("user3")
                    .introduction("today is fun")
                    .password("qwerqwer2@")
                    .name("홍길동")
                    .authority(COMMON)
                    .job(Job.builder()
                            .companyName("test company")
                            .address("test address")
                            .position("신입")
                            .build())
                    .build());
        }

        @DisplayName("팔로우 : 팔로우 등록시 팔로잉 유저의 팔로잉 수가 1 증가하고 팔로워 유저의 팔로워 수가 1 증가한다.")
        @Test
        public void follow() {
            //given
            var request = FollowRequest.builder()
                    .followerId(userB.getId())
                    .userId(userA.getId())
                    .build();
            //when
            followService.follow(request);
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

            var request = FollowCancelRequest.builder()
                    .followerId(userB.getId())
                    .followingId(userA.getId())
                    .build();
            //when
            followService.followCancel(request);
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

            var request = GetFollowingListRequest.builder()
                    .userId(user.getId())
                    .build();

            var pageable = PageRequest.of(0, 100);
            //when
            Page<FollowDto> list = followService.followingList(request, pageable);
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

            var request = GetFollowerListRequest.builder()
                    .userId(follower.getId())
                    .build();

            var pageable = PageRequest.of(0, 100);
            //when
            Page<FollowDto> list = followService.followerList(request, pageable);
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
            int count = followService.countFollowing(userA.getId());
            //then
            assertThat(count).isZero();
        }

        @DisplayName("팔로잉을 추가한 이후 팔로잉 수 증가가 반영되어 반환한다")
        @Test
        public void countFollowingWithFollow() {
            // given
            followService.follow(FollowRequest.builder()
                    .userId(userA.getId())
                    .followerId(userC.getId())
                    .build());
            // when
            int count = followService.countFollowing(userA.getId());
            // then
            assertThat(count).isOne();
        }

        @DisplayName("팔로잉이 없는 유저가 팔로잉 수를 조회할 때는 0을 반환한다")
        @Test
        public void countFollower() {
            //given //when
            int count = followService.countFollower(userA.getId());
            //then
            assertThat(count).isZero();
        }

        @DisplayName("팔로잉을 추가한 이후 팔로잉 수 증가가 반영되어 반환한다")
        @Test
        public void countFollowerWithFollow() {
            // given
            followService.follow(FollowRequest.builder()
                    .userId(userC.getId())
                    .followerId(userA.getId())
                    .build());
            // when
            int count = followService.countFollower(userA.getId());
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
