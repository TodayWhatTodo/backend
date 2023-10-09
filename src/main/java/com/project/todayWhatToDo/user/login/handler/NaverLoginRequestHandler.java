package com.project.todayWhatToDo.user.login.handler;

public class NaverLoginRequestHandler implements LoginRequestHandler {
    @Override
    public LoginResponseHandler getUserInfo(String token) {
        return new NaverLoginResponseHandler(null);
    }
}
