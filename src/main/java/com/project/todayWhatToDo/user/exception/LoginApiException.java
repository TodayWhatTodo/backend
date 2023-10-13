package com.project.todayWhatToDo.user.exception;

public class LoginApiException extends RuntimeException{
    public LoginApiException(String message) {
        super(message);
    }
}
