package com.project.todayWhatToDo.user.login;

import com.project.todayWhatToDo.user.exception.NotSupportAuthenticationProviderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.project.todayWhatToDo.user.login.LoginApiProvider.KAKAO;
import static com.project.todayWhatToDo.user.login.LoginApiProvider.NAVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginApiManagerTest {
    LoginApiManager manager = new LoginApiManager();

    @DisplayName("NAVER 로그인 요청 핸들러를 반환한다.")
    @Test
    public void getProviderWhenNaver() {
        // when
        LoginApiProvider provider = manager.getProvider("NAVER");
        // then
        assertThat(provider).isEqualTo(NAVER);
    }

    @DisplayName("KAKAO 로그인 요청 핸들러를 반환한다.")
    @Test
    public void getProviderWhenKakao() {
        // when
        LoginApiProvider provider = manager.getProvider("KAKAO");
        // then
        assertThat(provider).isEqualTo(KAKAO);
    }

    @DisplayName("제공하지 않은 프로바이더의 이름을 요청하면 예외가 발생한다.")
    @Test
    public void getProviderWhenNotExistProvider() {
        //when
        //then
        assertThatThrownBy(() -> manager.getProvider("hello"))
                .isInstanceOf(NotSupportAuthenticationProviderException.class);
    }
}
