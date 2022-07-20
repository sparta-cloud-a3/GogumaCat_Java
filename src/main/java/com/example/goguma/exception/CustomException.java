package com.example.goguma.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CustomException extends RuntimeException implements Serializable {

    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}