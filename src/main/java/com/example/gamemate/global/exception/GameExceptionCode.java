package com.example.gamemate.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GameExceptionCode implements ExceptionCode {

    GAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Game already exists"),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "Game not found"),
    GAME_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Game comment not found"),
    GAME_RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "Game rating not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "Invalid input provided"),
    UNAUTHORIZED_ACTION(HttpStatus.UNAUTHORIZED, "Unauthorized action"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
