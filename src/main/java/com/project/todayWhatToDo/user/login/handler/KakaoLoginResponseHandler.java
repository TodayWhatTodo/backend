package com.project.todayWhatToDo.user.login.handler;

import java.util.Map;

/**
 * 카카오 프로필 조회 api 응답 결과 반환 클래스
 *
 * @reference 카카오 api docs
 * <h3> 사용자 정보 가져오기 </h3>
 * <p> https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info </p>
 */
public class KakaoLoginResponseHandler extends LoginResponseHandler {
    public KakaoLoginResponseHandler(String response) {
        super(response);
    }

    private Map<String, Object> getProfile() {
        return (Map<String, Object>) getResponse().get("kakao_account");
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
