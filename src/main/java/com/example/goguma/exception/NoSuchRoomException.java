package com.example.goguma.exception;

public class NoSuchRoomException extends CustomException{

    public NoSuchRoomException() {
        super(ErrorCode.NO_SUCH_ROOM);
    }
}