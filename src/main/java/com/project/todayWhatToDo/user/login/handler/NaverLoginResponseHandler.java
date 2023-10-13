package com.project.todayWhatToDo.user.login.handler;

import java.util.Map;

/**
 * 네이버 프로필 조회 api 응답 결과 반환 클래스
 *
 * @reference 네이버 api docs
 * <h3> 1.6 네이버 사용자 프로필 정보 조회 </h3>
 * <p> https://developers.naver.com/docs/login/web/web.md </p>
 */
public class NaverLoginResponseHandler extends LoginResponseHandler {
    public NaverLoginResponseHandler(String response) {
        super(response);
    }

    private Map<String, Object> getProfile() {
        return (Map<String, Object>) getResponse().get("response");
    }

    @Override
    public String getEmail() {
        return (String) getProfile().get("email");
    }

    @Override
    public String getName() {
        return (String) getProfile().get("name");
    }

    @Override
    public String getPassword() {
        return "";
    }
}