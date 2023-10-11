package com.project.todayWhatToDo.notify.service;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.notify.domain.Notify;
import com.project.todayWhatToDo.notify.dto.CheckNotifyRequestDto;
import com.project.todayWhatToDo.notify.dto.DeleteNotifyRequestDto;
import com.project.todayWhatToDo.notify.dto.GetNotifyRequestDto;
import com.project.todayWhatToDo.notify.repository.NotifyRepository;
import com.project.todayWhatToDo.user.domain.Job;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.project.todayWhatToDo.security.Authority.COMMON;
import static org.assertj.core.api.Assertions.assertThat;

public class NotifyServiceTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    NotifyRepository notifyRepository;
    @Autowired
    NotifyService notifyService;

    User user;
    Notify notify1;
    Notify notify2;

    @BeforeEach
    public void saveData() {
        user = userRepository.saveAndFlush(User.builder()
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

        notify1 = notifyRepository.saveAndFlush(Notify.builder()
                .userId(user.getId())
                .content("test message1")
                .build());

        notify2 = notifyRepository.saveAndFlush(Notify.builder()
                .userId(user.getId())
                .content("test message2")
                .build());
    }

    @AfterEach
    public void removeAll() {
        notifyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("특정 유저에게 온 알람을 목록을 조회할 수 있다.")
    @Test
    public void findByUserId() {
        //given
        var request = GetNotifyRequestDto.builder()
                .userId(user.getId())
                .build();

        var pageable = PageRequest.of(0, 10);
        //when
        var result = notifyService.getNotifies(request, pageable);
        //then
        assertThat(result).extracting("content")
                .contains("test message1", "test message2");
    }

    @DisplayName("알람이 삭제되면 조회할 수 없다.")
    @Test
    public void deleteNotify() {
        //given
        var request = DeleteNotifyRequestDto.builder()
                .notifyIds(List.of(notify1.getId()))
                .userId(user.getId())
                .build();
        //when
        notifyService.delete(request);
        //then
        var result = notifyRepository.findByUserId(user.getId(), PageRequest.of(0, 100));
        assertThat(result).extracting("id")
                .doesNotContain(notify1.getId())
                .contains(notify2.getId());
    }

    @DisplayName("알람 확인을 하면 확인 상태가 true 이다.")
    @Test
    public void checkNotify() {
        //given
        var request = CheckNotifyRequestDto.builder()
                .id(notify1.getId())
                .build();
        //when
        notifyService.checkNotify(request);
        //then
        assertThat(notifyRepository.findById(notify1.getId()))
                .isNotEmpty()
                .get().extracting("isChecked")
                .isEqualTo(true);
    }
}
