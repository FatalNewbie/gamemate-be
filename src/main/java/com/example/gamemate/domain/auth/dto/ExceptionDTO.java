package com.example.gamemate.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionDTO {

    private String errorCode;
    private String errorMessage;

    public ExceptionDTO(String errorCode, String errorMessage) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;

    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
