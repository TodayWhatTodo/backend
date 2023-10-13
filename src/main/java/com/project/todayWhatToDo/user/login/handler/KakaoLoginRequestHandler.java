package com.project.todayWhatToDo.user.login.handler;

import com.project.todayWhatToDo.user.exception.LoginApiException;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class KakaoLoginRequestHandler implements LoginRequestHandler {
    @Override
    public LoginResponseHandler getUserInfo(RestTemplate restTemplate, String token) {
        var requestEntity = RequestEntity.post("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", getBearerToken(token))
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .acceptCharset(StandardCharsets.UTF_8)
                .body("property_keys=[\"kakao_account.email\", \"kakao_account.name\"]");

        var response = restTemplate.exchange(requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new LoginApiException("네이버 oauth 로그인 실패");
        }
        return new KakaoLoginResponseHandler(response.getBody());
    }

    private String getBearerToken(String token) {
        if (token.startsWith("Bearer")) return token;
        else return "Bearer" + token;
    }

}
