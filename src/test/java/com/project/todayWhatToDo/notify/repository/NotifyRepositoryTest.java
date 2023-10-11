package com.project.todayWhatToDo.notify.repository;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.notify.domain.Notify;
import com.project.todayWhatToDo.user.domain.Job;
import com.project.todayWhatToDo.user.domain.User;
import com.project.todayWhatToDo.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.todayWhatToDo.security.Authority.COMMON;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class NotifyRepositoryTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    NotifyRepository notifyRepository;

    User user;
    Notify notify1;
    Notify notify2;

    @BeforeEach
    public void saveData() {
        user = userRepository.saveAndFlush(User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .job(Job.builder()
                        .address("test address")
                        .position("신입")
                        .companyName("test company")
                        .build())
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(COMMON)
                .build());

        notify1 = notifyRepository.save(Notify.builder()
                .userId(user.getId())
                .content("test message1")
                .build());

        notify2 = notifyRepository.save(Notify.builder()
                .userId(user.getId())
                .content("test message2")
                .build());
    }

    @DisplayName("특정 유저에게 온 알람을 목록을 조회할 수 있다.")
    @Test
    public void findByUserId() {
        //given
        var pageable = PageRequest.of(0, 10);
        //when
        Page<Notify> result = notifyRepository.findByUserId(user.getId(), pageable);
        //then
        assertThat(result).extracting("userId", "content")
                .contains(
                        Tuple.tuple(user.getId(), "test message1"),
                        Tuple.tuple(user.getId(), "test message2")
                );
    }

    @DisplayName("알람을 삭제한다.")
    @Test
    public void deleteAllByIdAndUserId() {
        //given
        var deleteNotifies = List.of(notify1.getId(), notify2.getId());
        //when
        notifyRepository.deleteAllByIdInAndUserId(deleteNotifies, user.getId());
        //then
        var result = notifyRepository.findByUserId(user.getId(), PageRequest.of(0, 10000));
        assertThat(result).hasSize(0);
    }
}
