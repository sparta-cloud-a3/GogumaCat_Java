package com.example.goguma.exception;

public class LoginException extends CustomException{
    public LoginException() {
        super(ErrorCode.LOGIN);
    }
}
