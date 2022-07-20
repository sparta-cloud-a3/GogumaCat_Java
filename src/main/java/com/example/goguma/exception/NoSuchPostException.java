package com.example.goguma.exception;

public class NoSuchPostException extends CustomException{

    public NoSuchPostException() {
        super(ErrorCode.NO_SUCH_POST);
    }
}
