package com.project.todayWhatToDo.user.login;

import com.project.todayWhatToDo.user.login.handler.KakaoLoginRequestHandler;
import com.project.todayWhatToDo.user.login.handler.LoginRequestHandler;
import com.project.todayWhatToDo.user.login.handler.LoginResponseHandler;
import com.project.todayWhatToDo.user.login.handler.NaverLoginRequestHandler;
import org.springframework.web.client.RestTemplate;

public enum LoginApiProvider implements LoginRequestHandler {


    NAVER(new NaverLoginRequestHandler()),
    KAKAO(new KakaoLoginRequestHandler());

    private final LoginRequestHandler strategy;

    LoginApiProvider(LoginRequestHandler strategy) {
        this.strategy = strategy;
    }

    @Override
    public LoginResponseHandler getUserInfo(RestTemplate restTemplate, String token) {
        return this.strategy.getUserInfo(restTemplate, token);
    }
}
