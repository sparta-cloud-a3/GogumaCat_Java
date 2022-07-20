package com.example.goguma.exception;

public class NoTokenException extends CustomException{

    public NoTokenException() {
        super(ErrorCode.NO_TOKEN);
    }
}
