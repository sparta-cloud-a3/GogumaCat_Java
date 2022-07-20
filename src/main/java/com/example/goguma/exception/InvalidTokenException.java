package com.example.goguma.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException() {
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
        log.error(errorCode.getCode() + " : " + errorCode.getMessage());
    }
}
