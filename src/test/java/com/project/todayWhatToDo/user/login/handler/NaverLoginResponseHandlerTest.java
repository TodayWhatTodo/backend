package com.project.todayWhatToDo.user.login.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("네이버 로그인 API Response handler test")
public class NaverLoginResponseHandlerTest {

    String response = """
            {
              "resultcode": "00",
              "message": "success",
              "response": {
                "email": "test@naver.com",
                "nickname": "OpenAPI",
                "profile_image": "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
                "age": "40-49",
                "gender": "F",
                "id": "32742776",
                "name": "홍길동",
                "birthday": "10-01"
              }
            }
            """;

    @DisplayName("이메일을 조회한다.")
    @Test
    public void getEmail() {
        //given
        var handler = new NaverLoginResponseHandler(response);
        //when
        var email = handler.getEmail();
        //then
        assertThat(email).isEqualTo("test@naver.com");
    }

    @DisplayName("이름을 조회한다.")
    @Test
    public void getName() {
        //given
        var handler = new NaverLoginResponseHandler(response);
        //when
        var name = handler.getName();
        //then
        assertThat(name).isEqualTo("홍길동");
    }
}