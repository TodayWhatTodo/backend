package com.project.todayWhatToDo.user.domain;

import com.project.todayWhatToDo.user.dto.UpdateCareerRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("회사 경력 도메인 테스트")
public class CareerTest {

    @Nested
    @DisplayName("수정 테스트")
    class Update {
        Career career = Career.builder()
                .startedAt(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                .endedAt(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
                .introduction("first introduction")
                .position("신입")
                .user(null)
                .company(Company.builder()
                        .name("아무개 회사")
                        .address("서울 강남구")
                        .build())
                .build();

        @DisplayName("소개글이 변경된다")
        @Test
        public void updateIntroduction() {
            //given
            var request = UpdateCareerRequestDto.builder()
                    .introduction("정말 좋은 회사야 도망쳐..")
                    .build();
            //when
            career.update(request);
            //then
            assertThat(career.getIntroduction()).isEqualTo("정말 좋은 회사야 도망쳐..");
        }

        @DisplayName("입사일이 변경된다")
        @Test
        public void updateStartedAt() {
            //given
            LocalDateTime after = LocalDateTime.of(1000, 1, 1, 1, 1, 1);
            var request = UpdateCareerRequestDto.builder()
                    .startedAt(after)
                    .build();
            //when
            career.update(request);
            //then
            assertThat(career.getStartedAt()).isEqualTo(after);
        }

        @DisplayName("입사일이 퇴사일 보다 이후라면 실패한다.")
        @Test
        public void updateStartedAtFail() {
            //given
            LocalDateTime after = LocalDateTime.of(2111, 1, 1, 1, 1, 1);
            var request = UpdateCareerRequestDto.builder()
                    .startedAt(after)
                    .build();
            //when //then
            assertThatThrownBy(() -> career.update(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("퇴사일이 변경된다")
        @Test
        public void updateEndedAt() {
            //given
            LocalDateTime after = LocalDateTime.of(2100, 1, 1, 1, 1, 1);
            var request = UpdateCareerRequestDto.builder()
                    .endedAt(after)
                    .build();
            //when
            career.update(request);
            //then
            assertThat(career.getEndedAt()).isEqualTo(after);
        }

        @DisplayName("퇴사일이 입사일 보다 뒤면 예외가 발생한다.")
        @Test
        public void updateEndedAtFail() {
            //given
            LocalDateTime after = LocalDateTime.of(1000, 1, 1, 1, 1, 1);
            var request = UpdateCareerRequestDto.builder()
                    .endedAt(after)
                    .build();
            //when //then
            assertThatThrownBy(() -> career.update(request));
        }

        @DisplayName("변경 내용이 없다면 무시 된다")
        @Test
        public void nonUpdate() {
            //given
            var request = UpdateCareerRequestDto.builder().build();
            //when
            career.update(request);
            //then
            assertThat(career).extracting("introduction", "startedAt", "endedAt", "position")
                    .doesNotContainNull();
        }
    }
}
