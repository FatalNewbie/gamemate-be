package com.example.gamemate.global.exception;

public class InvalidUserIdException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public InvalidUserIdException(String message) {
        super(message);
        this.exceptionCode = FriendExceptionCode.INVALID_USER_ID;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
