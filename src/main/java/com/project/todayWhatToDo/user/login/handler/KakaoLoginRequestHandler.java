package com.project.todayWhatToDo.user.login.handler;

public class KakaoLoginRequestHandler implements LoginRequestHandler {
    @Override
    public LoginResponseHandler getUserInfo(String token) {
        return new KakaoLoginResponseHandler(null);
    }
}
