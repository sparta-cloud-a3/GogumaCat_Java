package com.example.goguma.exception;

public class NoSuchUserException extends CustomException{
    public NoSuchUserException() {
        super(ErrorCode.NO_SUCH_USER);
    }
}
