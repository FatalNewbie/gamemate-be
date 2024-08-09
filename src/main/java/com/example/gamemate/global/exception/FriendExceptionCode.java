package com.example.gamemate.global.exception;

import org.springframework.http.HttpStatus;

public enum FriendExceptionCode implements ExceptionCode {
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "Invalid user ID provided."),
    INVALID_FRIEND_RELATIONSHIP(HttpStatus.BAD_REQUEST, "Invalid friend relationship."),
    INVALID_FRIEND_STATUS(HttpStatus.BAD_REQUEST, "Invalid friend status.");

    private final HttpStatus httpStatus;
    private final String message;

    FriendExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
    }
