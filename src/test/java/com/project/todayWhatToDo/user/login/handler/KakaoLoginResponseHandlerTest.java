package com.project.todayWhatToDo.user.login.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("카카오 로그인 API Response handler test")
public class KakaoLoginResponseHandlerTest {

    String response = """
            {
                "id":123456789,
                "kakao_account": {\s
                    "profile_needs_agreement": false,
                    "profile": {
                        "nickname": "test",
                        "thumbnail_image_url": "http://yyy.kakao.com/.../img_110x110.jpg",
                        "profile_image_url": "http://yyy.kakao.com/dn/.../img_640x640.jpg",
                        "is_default_image":false
                    },
                    "name": "홍길동",
                    "email": "test@naver.com",
                    "age_range":"20~29",
                    "birthday":"1130",
                    "gender":"female"
                }
            }
            """;

    @DisplayName("이메일을 조회한다.")
    @Test
    public void getEmail() {
        //given
        var handler = new KakaoLoginResponseHandler(response);
        //when
        var email = handler.getEmail();
        //then
        assertThat(email).isEqualTo("test@naver.com");
    }

    @DisplayName("이름을 조회한다.")
    @Test
    public void getName() {
        //given
        var handler = new KakaoLoginResponseHandler(response);
        //when
        var name = handler.getName();
        //then
        assertThat(name).isEqualTo("홍길동");
    }
}