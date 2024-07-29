package com.green.project2nd.review.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class MyResponse<T> {
    private HttpStatus statusCode;
    private String resultMsg;
    private int code;
    //private T resultData;
}
