package com.example.gamemate.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계정"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호 오류"),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "삭제된 계정");

    private final HttpStatus httpStatus;
    private final String message;
}