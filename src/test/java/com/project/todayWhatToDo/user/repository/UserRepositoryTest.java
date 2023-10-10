package com.project.todayWhatToDo.user.repository;

import com.project.todayWhatToDo.IntegrationTest;
import com.project.todayWhatToDo.security.Authority;
import com.project.todayWhatToDo.user.domain.Career;
import com.project.todayWhatToDo.user.domain.Company;
import com.project.todayWhatToDo.user.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class UserRepositoryTest extends IntegrationTest {

    @Autowired
    UserRepository repository;
    @Autowired
    EntityManager em;

    @DisplayName("이메일을 통해 유저를 조회할 수 있다.")
    @Test
    public void findByEmail() {
        //given
        User user = repository.save(User.builder()
                .email("today@naver.com")
                .nickname("today")
                .password("qwerqwer2@")
                .name("홍길동")
                .build());
        //when
        Optional<User> find = repository.findByEmail(user.getEmail());
        //then
        assertThat(find).isNotEmpty()
                .get().extracting("email", "nickname", "password", "name")
                .containsExactly("today@naver.com", "today", "qwerqwer2@", "홍길동");
    }

    @DisplayName("없는 이메일을 통해 유저 조회시 결과는 없다.")
    @Test
    public void findByEmailNotExist() {
        //when
        Optional<User> find = repository.findByEmail("hello@naver.com");
        //then
        assertThat(find).isEmpty();
    }

    @DisplayName("이메일, 이름, 비밀번호가 일치하는 유저를 찾는다.")
    @Test
    public void findByEmailAndNameAndPassword() {
        //given
        User user = repository.save(User.builder()
                .email("today@naver.com")
                .nickname("today")
                .password("qwerqwer2@")
                .name("홍길동")
                .build());
        //when
        Optional<User> find = repository.findByEmailAndNameAndPassword(user.getEmail(), user.getName(), user.getPassword());
        //then
        assertThat(find).isNotEmpty()
                .get().extracting("email", "nickname", "password", "name")
                .containsExactly("today@naver.com", "today", "qwerqwer2@", "홍길동");
    }

    @DisplayName("이메일, 이름, 비밀번호가 일치하지 않은 유저가 없다면 결과는 없다.")
    @Test
    public void findByEmailAndNameAndPasswordNotExist() {
        //when
        Optional<User> find = repository.findByEmailAndNameAndPassword("hello@naver.com", "", "");
        //then
        assertThat(find).isEmpty();
    }

    @DisplayName("user에 회사 경력을 저장할 수 있다.")
    @Test
    public void saveCareer() {
        //given
        var user = repository.save(User.builder()
                .email("today@naver.com")
                .nickname("today")
                .introduction("today is fun")
                .password("qwerqwer2@")
                .name("홍길동")
                .authority(Authority.COMMON)
                .build());

        LocalDateTime startedAt = LocalDateTime.of(2000, 10, 10, 10, 10, 10);
        LocalDateTime endedAt = LocalDateTime.of(2001, 10, 10, 10, 10, 10);

        var career = Career.builder()
                .user(user)
                .company(Company.builder()
                        .name("todo company")
                        .address("address")
                        .build()
                )
                .introduction("my first job")
                .startedAt(startedAt)
                .endedAt(endedAt)
                .position("대리")
                .build();
        //when
        user.addCareer(career);
        repository.flush();
        //then
        assertThat(em.find(Career.class, career.getId()))
                .extracting("introduction", "startedAt", "endedAt", "position", "company")
                .containsExactly("my first job", startedAt, endedAt, "대리", Company.builder()
                        .name("todo company")
                        .address("address")
                        .build());
    }
}
