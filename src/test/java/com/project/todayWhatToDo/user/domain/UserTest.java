package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.security.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.todayWhatToDo.security.Authority.QUIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

public class UserTest {


    @DisplayName("팔로워 수와 팔로잉 수의 초기 값은 0이다.")
    @Test
    public void defaultFollowCacheValue() {
        //given
        var user = User.builder().build();
        //when //then
        assertThat(user.getFollowerCount()).isZero();
        assertThat(user.getFollowingCount()).isZero();
    }

    @DisplayName("getter method test")
    @Test
    void getter() {
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

    @DisplayName("커리어 리스트 조회 내용을 변경할 수 없다.")
    @Test
    public void getCareer() {
        //given
        var user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();
        //when
        List<Career> careers = user.getCareers();
        //then
        assertThatThrownBy(() -> careers.add(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @DisplayName("커리어를 추가시에 user를 통해서 해당 커리어가 조회된다.")
    @Test
    public void addCareer() {
        //given
        var user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();

        LocalDateTime startedAt = LocalDateTime.of(2000, 10, 10, 10, 10, 10);
        LocalDateTime endedAt = LocalDateTime.of(2001, 10, 10, 10, 10, 10);

        var career = Career.builder()
                .user(user)
                .job(Job.builder()
                        .companyName("todo company")
                        .address("test address")
                        .position("대리")
                        .build()
                )
                .introduction("my first job")
                .startedAt(startedAt)
                .endedAt(endedAt)
                .build();
        //when
        user.addCareer(career);
        //then
        assertThat(user.getCareers())
                .extracting("introduction", "startedAt", "endedAt", "job.position", "job.companyName", "job.address")
                .contains(tuple("my first job", startedAt, endedAt, "대리", "todo company", "test address"));
    }

    @DisplayName("setter 함수 사용시")
    @Nested
    class setterTest {

        User user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .job(Job.builder()
                        .companyName("before company")
                        .address("test address")
                        .build())
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();

        @DisplayName("nickname이 변경된다.")
        @Test
        public void nickname() {
            //given //when
            user.setNickname("after nickname");
            //then
            assertThat(user.getNickname()).isEqualTo("after nickname");
        }


        @DisplayName("introduction 변경된다.")
        @Test
        public void introduction() {
            //given //when
            user.setIntroduction("after introduction");
            //then
            assertThat(user.getIntroduction()).isEqualTo("after introduction");
        }

        @DisplayName("재직 회사를 변경한다.")
        @Test
        public void getCompanyName() {
            //given //when
            user.setJob(Job.builder()
                    .companyName("after company")
                    .address("test address")
                    .build());
            //then
            assertThat(user.getJob()).extracting("companyName")
                    .isEqualTo("after company");
        }

        @DisplayName("프로필 이미지 경로를 변경한다.")
        @Test
        public void imagePath() {
            //given //when
            user.setImagePath("after image path");
            //then
            assertThat(user.getImagePath())
                    .isEqualTo("after image path");
        }

        @DisplayName("setter input이 null 이라면 변경하지 않는다.")
        @Test
        public void setterWhenNull() {
            //given //when
            String beforeIntroduction = user.getIntroduction();
            String beforeNickname = user.getNickname();
            String beforeCompanyName = user.getJob().getCompanyName();
            String beforeImagePath = user.getImagePath();

            user.setNickname(null);
            user.setIntroduction(null);
            user.setJob(Job.builder()
                    .companyName(null)
                    .build());
            user.setImagePath(null);
            //then
            assertThat(user.getIntroduction()).isEqualTo(beforeIntroduction);
            assertThat(user.getNickname()).isEqualTo(beforeNickname);
            assertThat(user.getJob().getCompanyName()).isEqualTo(beforeCompanyName);
            assertThat(user.getImagePath()).isEqualTo(beforeImagePath);
        }
    }

    @DisplayName("상태 관리가 필요한 데이터를 반환한다.")
    @Test
    public void toSession() {
        //given
        User user = spy(User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .job(Job.builder()
                        .companyName("before company")
                        .address("test address")
                        .build()
                )
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build());

        given(user.getId()).willReturn(1L);
        //when
        var session = user.toSession();
        //then
        assertThat(session.email()).isEqualTo("today@naver.com");
        assertThat(session.name()).isEqualTo("홍길동");
        assertThat(session.id()).isEqualTo(1L);
    }

    @DisplayName("reduceFollowing 호출시 팔로잉 수가 1 감소한다.")
    @Test
    public void reduceFollowing() {
        //given
        User user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .job(Job.builder()
                        .companyName("before company")
                        .address("test address")
                        .build()
                )
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();
        //when
        var before = user.getFollowingCount();
        user.reduceFollowing();
        //then
        assertThat(user.getFollowingCount()).isEqualTo(before - 1);
    }

    @DisplayName("reduceFollower 호출시 팔로워 수가 1 감소한다.")
    @Test
    public void reduceFollower() {
        //given
        User user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .job(Job.builder()
                        .companyName("before company")
                        .address("test address")
                        .build()
                )
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();
        //when
        var before = user.getFollowerCount();
        user.reduceFollower();
        //then
        assertThat(user.getFollowerCount()).isEqualTo(before - 1);
    }

    @DisplayName("프로필 정보를 담은 객체를 반환한다.")
    @Test
    public void toProfile() {
        //given
        User user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .job(Job.builder()
                        .companyName("before company")
                        .address("test address")
                        .build()
                )
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build();
        //when
        var profile = user.toProfile();
        //then
        assertThat(profile)
                .extracting("profileImagePath", "followerCount", "followingCount", "nickname",
                        "introduction", "position", "company")
                .containsExactly(user.getImagePath(), user.getFollowerCount(), user.getFollowingCount(), user.getNickname(),
                        user.getIntroduction(), user.getJob().getPosition(), user.getJob().getCompanyName());
    }

    @DisplayName("탈퇴시 권한은 QUIT으로 변경된다.")
    @Test
    public void quit() {
        // given
        var user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .password("qwerqwer2@")
                .name("홍길동")
                .build();
        // when
        user.quit();
        // then
        assertThat(user.getAuthority()).isEqualTo(QUIT);
    }
}
