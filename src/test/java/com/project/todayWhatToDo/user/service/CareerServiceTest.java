package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.user.domain.Career;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.UpdateCareerRequestDto;
import com.project.todayWhatToDo.user.repository.CareerRepository;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CareerServiceTest extends IntegrationTest {

    @Autowired
    CareerService careerService;
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    UserRepository userRepository;

    @DisplayName("경력 사항을 변경할 수 있다.")
    @Test
    public void update() {
        //given
        var user = userRepository.saveAndFlush(User.builder()
                .email("test@naver.com")
                .nickname("hello")
                .name("홍길동")
                .password("test")
                .build());

        var career = careerRepository.saveAndFlush(Career.builder()
                .startedAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                .user(user)
                .build());

        var req = UpdateCareerRequestDto.builder()
                .startedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .endedAt(LocalDateTime.of(2023, 1, 1, 0, 0, 0))
                .careerId(career.getId())
                .introduction("반가워")
                .position("대리")
                .build();
        //when
        careerService.updateCareer(req, user.getId());
        //then
        assertThat(careerRepository.findByIdAndUserId(career.getId(), user.getId()))
                .isNotEmpty()
                .get()
                .extracting("startedAt", "endedAt", "user.id", "introduction", "position")
                .containsExactly(req.startedAt(), req.endedAt(), user.getId(), req.introduction(), req.position());
    }

    @DisplayName("경력 삭제시 데이터가 삭제된다")
    @Test
    public void delete() {
        //given
        var user = userRepository.saveAndFlush(User.builder()
                .email("test@naver.com")
                .nickname("hello")
                .name("홍길동")
                .password("test")
                .build());

        var career = careerRepository.saveAndFlush(Career.builder()
                .startedAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                .user(user)
                .build());
        //when
        careerService.deleteCareer(career.getId(), user.getId());
        //then
        assertThat(careerRepository.findByIdAndUserId(career.getId(), user.getId()))
                .isEmpty();
    }
}
