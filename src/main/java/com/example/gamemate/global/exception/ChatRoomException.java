package com.example.gamemate.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomException extends RuntimeException{
    private final ExceptionCode exceptionCode;
}
