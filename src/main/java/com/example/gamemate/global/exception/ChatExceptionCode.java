package com.example.gamemate.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatExceptionCode implements ExceptionCode{
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ChatRoom does not exist"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
