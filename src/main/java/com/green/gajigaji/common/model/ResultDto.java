package com.green.gajigaji.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDto<T> {
    private HttpStatus status;
    private int code;
    private String resultMsg;
    private T resultData;
    public static <T> ResultDto<T> resultDto(HttpStatus status, int code, String resultMsg) {
        return ResultDto.<T>builder().status(status).code(code).resultMsg(resultMsg).build();
    }
    public static <T> ResultDto<T> resultDto(HttpStatus status, int code, String resultMsg, T resultData) {
        return ResultDto.<T>builder().status(status).code(code).resultMsg(resultMsg).resultData(resultData).build();
    }

}