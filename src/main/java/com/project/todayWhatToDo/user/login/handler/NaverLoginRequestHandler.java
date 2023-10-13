package com.project.todayWhatToDo.user.login.handler;

import com.project.todayWhatToDo.user.exception.LoginApiException;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

public class NaverLoginRequestHandler implements LoginRequestHandler {
    @Override
    public LoginResponseHandler getUserInfo(RestTemplate restTemplate, String token) {
        var requestEntity = RequestEntity.get("https://openapi.naver.com/v1/nid/me")
                .header("Authorization", getBearerToken(token))
                .build();

        var response = restTemplate.exchange(requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new LoginApiException("네이버 oauth 로그인 실패");
        }
        return new NaverLoginResponseHandler(response.getBody());
    }

    private String getBearerToken(String token) {
        if (token.startsWith("Bearer")) return token;
        else return "Bearer" + token;
    }

}
