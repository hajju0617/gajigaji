package com.green.gajigaji.review.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getMessage();
    int getCode();
}
