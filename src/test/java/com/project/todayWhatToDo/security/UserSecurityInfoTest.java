package com.project.todayWhatToDo.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSecurityInfoTest {

    UserSecurityInfo info;

    public UserSecurityInfoTest() {
        info = UserSecurityInfo.builder()
                .email("hello@naver.com")
                .password("qwerqwer2@")
                .authority("USER")
                .build();
    }

    @DisplayName("유저의 권한을 반환한다.")
    @Test
    public void getAuthorities() {
        Collection<? extends GrantedAuthority> authorities = info.getAuthorities();

        assertThat(authorities).extracting("authority")
                .contains("USER");
    }

    @DisplayName("getUsername : 이메일을 반환한다.")
    @Test
    public void getUsername() {
        //when //then
        assertThat(info.getUsername()).isEqualTo("hello@naver.com");
    }

    @DisplayName("getPassword : 비밀번호를 반환한다.")
    @Test
    public void getPassword() {
        //when //then
        assertThat(info.getUsername()).isEqualTo("hello@naver.com");
    }

    @DisplayName("isAccountNonExpired : 계정 만료는 되지 않는다.")
    @Test
    public void isAccountNonExpired() {
        //when //then
        assertThat(info.isAccountNonLocked()).isTrue();
    }

    @DisplayName("isAccountNonLocked : 계정 잠금이 되지 않는다.")
    @Test
    public void isAccountNonLocked() {
        //when //then
        assertThat(info.isAccountNonLocked()).isTrue();
    }

    @DisplayName("isCredentialsNonExpired : 자격이 만료 되지 않는다.")
    @Test
    public void isCredentialsNonExpired() {
        //when //then
        assertThat(info.isCredentialsNonExpired()).isTrue();
    }

    @DisplayName("isEnabled : 계정은 항상 사용가능하다.")
    @Test
    public void isEnabled() {
        //when //then
        assertThat(info.isEnabled()).isTrue();
    }
}
