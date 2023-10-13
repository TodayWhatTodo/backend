package com.project.todayWhatToDo.user.login.handler;

import org.springframework.web.client.RestTemplate;

public interface LoginRequestHandler {

    LoginResponseHandler getUserInfo(RestTemplate restTemplate, String token);


}

