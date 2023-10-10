package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.security.Authority;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                .name("todo company")
                .introduction("my first job")
                .startedAt(startedAt)
                .endedAt(endedAt)
                .position("대리")
                .build();
        //when
        user.addCareer(career);
        //then
        assertThat(user.getCareers()).extracting("introduction", "startedAt", "endedAt", "position", "name")
                .contains(Tuple.tuple("my first job", startedAt, endedAt, "대리", "todo company"));
    }

    @DisplayName("setter 함수 사용시")
    @Nested
    class setterTest{

        User user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .companyName("before company")
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

        @DisplayName("재직 회사명이 변경된다.")
        @Test
        public void companyName() {
            //given //when
            user.setCompanyName("after company");
            //then
            assertThat(user.getCompanyName()).isEqualTo("after company");
        }

        @DisplayName("setter input이 null 이라면 변경하지 않는다.")
        @Test
        public void setterWhenNull() {
            //given //when
            String beforeIntroduction = user.getIntroduction();
            String beforeNickname = user.getNickname();
            user.setNickname(null);
            user.setIntroduction(null);
            //then
            assertThat(user.getIntroduction())
                    .isNotNull()
                    .isEqualTo(beforeIntroduction);
            assertThat(user.getNickname())
                    .isNotNull()
                    .isEqualTo(beforeNickname);
        }
    }
}
