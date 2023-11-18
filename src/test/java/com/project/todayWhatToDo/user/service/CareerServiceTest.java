package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.user.domain.Career;
import com.project.todayWhatToDo.user.domain.Job;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.dto.request.CreateCareerRequest;
import com.project.todayWhatToDo.user.dto.request.UpdateCareerRequest;
import com.project.todayWhatToDo.user.repository.CareerRepository;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.project.todayWhatToDo.security.Authority.COMMON;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경력 서비스 테스트")
@Transactional
public class CareerServiceTest extends IntegrationTest {

    @Autowired
    CareerService careerService;
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    UserRepository userRepository;

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
                .job(Job.builder()
                        .companyName("test company")
                        .address("test address")
                        .position("신입")
                        .build())
                .build());

        var startedAt = LocalDateTime.of(2000, 10, 10, 10, 10, 10);
        var endedAt = LocalDateTime.of(2001, 10, 10, 10, 10, 10);
        var request = new CreateCareerRequest(user.getId(), "todo company", "test address", "my first job", startedAt, endedAt, "대리");
        //when
        careerService.createCareer(request);
        //then
        assertThat(user.getCareers()).extracting("introduction", "startedAt", "endedAt", "job.position")
                .contains(Tuple.tuple("my first job", startedAt, endedAt, "대리"));

    }

    @DisplayName("경력 사항을 변경할 수 있다.")
    @Test
    public void update() {
        //given
        var user = userRepository.saveAndFlush(User.builder()
                .email("test@naver.com")
                .nickname("hello")
                .name("홍길동")
                .password("test")
                .job(Job.builder()
                        .companyName("test company")
                        .address("test address")
                        .position("신입")
                        .build())
                .build());

        var career = careerRepository.saveAndFlush(Career.builder()
                .startedAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                .user(user)
                .job(Job.builder()
                        .companyName("test company")
                        .address("test address")
                        .position("신입")
                        .build())
                .build());

        var req = UpdateCareerRequest.builder()
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
                .extracting("startedAt", "endedAt", "user.id", "introduction", "job.position")
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
                .job(Job.builder()
                        .companyName("test company")
                        .address("test address")
                        .position("신입")
                        .build())
                .build());

        var career = careerRepository.saveAndFlush(Career.builder()
                .startedAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                .user(user)
                .job(Job.builder()
                        .companyName("test company")
                        .address("test address")
                        .position("신입")
                        .build())
                .build());
        //when
        careerService.deleteCareer(career.getId(), user.getId());
        //then
        assertThat(careerRepository.findByIdAndUserId(career.getId(), user.getId()))
                .isEmpty();
    }
}
