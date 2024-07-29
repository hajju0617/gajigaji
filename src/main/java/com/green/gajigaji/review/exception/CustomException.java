package com.green.gajigaji.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
}
