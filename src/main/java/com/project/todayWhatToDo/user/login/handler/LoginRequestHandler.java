package com.project.todayWhatToDo.user.login.handler;

public interface LoginRequestHandler {

    LoginResponseHandler getUserInfo(String token);


}

