package com.project.todayWhatToDo.user.exception;

/**
 * Oauth 인증 서비스를 제공하지 않은 경우 사용하는 예외
 * 예시) 카카오 인증만 제공하는데 네이버 인증을 요청하는 경우에 사용한다.
 */
public class NotSupportAuthenticationProviderException extends RuntimeException{
}
