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
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.spy;

public class UserTest {

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
                .company(Company.builder()
                        .name("todo company")
                        .address("test address")
                        .build()
                )
                .introduction("my first job")
                .startedAt(startedAt)
                .endedAt(endedAt)
                .position("대리")
                .build();
        //when
        user.addCareer(career);
        //then
        assertThat(user.getCareers()).extracting("introduction", "startedAt", "endedAt", "position", "company")
                .contains(Tuple.tuple("my first job", startedAt, endedAt, "대리",
                        Company.builder()
                                .name("todo company")
                                .address("test address")
                                .build()));
    }

    @DisplayName("setter 함수 사용시")
    @Nested
    class setterTest {

        User user = User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .company(Company.builder()
                        .name("before company")
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
        public void company() {
            //given //when
            user.setCompany(Company.builder()
                    .name("after company")
                    .address("test address")
                    .build());
            //then
            assertThat(user.getCompany()).extracting("name")
                    .isEqualTo("after company");
        }

        @DisplayName("setter input이 null 이라면 변경하지 않는다.")
        @Test
        public void setterWhenNull() {
            //given //when
            String beforeIntroduction = user.getIntroduction();
            String beforeNickname = user.getNickname();
            String beforeCompanyName = user.getCompany().getName();
            user.setNickname(null);
            user.setIntroduction(null);
            user.setCompany(Company.builder()
                            .name(null)
                    .build());
            //then
            assertThat(user.getIntroduction())
                    .isNotNull()
                    .isEqualTo(beforeIntroduction);
            assertThat(user.getNickname())
                    .isNotNull()
                    .isEqualTo(beforeNickname);
            assertThat(user.getCompany().getName())
                    .isNotNull()
                    .isEqualTo(beforeCompanyName);
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
                .company(Company.builder()
                        .name("before company")
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
}
