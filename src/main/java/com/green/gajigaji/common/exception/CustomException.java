package com.green.gajigaji.common.exception;

import lombok.Getter;

/*
    RuntimeException + ErrorCode를 implements한 객체 주소값을 담을 수 있는 기능
 */

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}


