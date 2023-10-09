package com.project.todayWhatToDo.user.login.handler;

import java.util.Map;

public class KakaoLoginResponseHandler extends LoginResponseHandler {
    public KakaoLoginResponseHandler(Map<String, Object> response) {
        super(response);
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
