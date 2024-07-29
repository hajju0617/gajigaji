package com.green.gajigaji.review.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order(1)
@RestControllerAdvice(basePackages = "com.green.gajigaji.review")
public class ReviewGlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleException(CustomException e) {
        log.error("CustomException - handlerException: {}, e");
        return handleExceptionInternal(e.getErrorCode(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException() {
        log.error("UnidentifiedException - handlerException: {}, e");
        return handleExceptionInternal(ReviewErrorCode.UNIDENTIFIED_ERROR, null);
    }

    //handleExceptionInternal
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, BindException e) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, e));
    }

    private MyErrorResponse makeErrorResponse(ErrorCode errorCode, BindException e) {
        return MyErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus())
                .resultMsg(errorCode.getMessage())
                .code(errorCode.getCode())
                .valids(e == null ? null : getValidationError(e))
                .build();
    }

    private List<MyErrorResponse.ValidationError> getValidationError(BindException e) {
        List<MyErrorResponse.ValidationError> list = new ArrayList();

        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        for(FieldError fieldError : fieldErrorList) {
            MyErrorResponse.ValidationError validError = MyErrorResponse.ValidationError.of(fieldError);
            list.add(validError);
        }
        return list;
    }
}
