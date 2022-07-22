package com.example.goguma.exception;

public class NoSuchOrderException extends CustomException{
    public NoSuchOrderException() {
        super(ErrorCode.NO_SUCH_ORDER);
    }
}
