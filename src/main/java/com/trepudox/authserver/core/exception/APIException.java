package com.trepudox.authserver.core.exception;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException {

    private final String title;
    private final String detail;
    private final int status;

    public APIException(String title, String detail, int status) {
        super(detail);
        this.title = title;
        this.detail = detail;
        this.status = status;
    }

}
