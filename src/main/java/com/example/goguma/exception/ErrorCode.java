package com.example.goguma.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    NO_SUCH_POST(400, "P001", "존재하지 않는 게시물 입니다."),
    NO_SUCH_ROOM(400, "R001", "존재하지 않는 채팅방 입니다."),
    NO_SUCH_USER(400, "U001", "존재하지 않는 사용자 입니다."),
    LOGIN(400, "U002", "아이디나 비밀번호가 일치하지 않습니다."),
    INTERNAL_SERVER_ERROR(500, "500", "서버 에러");

    private int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}