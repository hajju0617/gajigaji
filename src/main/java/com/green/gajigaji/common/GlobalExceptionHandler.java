package com.green.gajigaji.common;

import com.green.gajigaji.common.model.ResultDto;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.green.gajigaji.user.userexception.ConstMessage.FAILURE;

@Order(2)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    //1.런타임
    @ExceptionHandler(RuntimeException.class)
    public ResultDto<String> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return ResultDto.resultDto(HttpStatus.BAD_GATEWAY,2, "RuntimeException : 처리할 수 없는 요청입니다.");
    }
    @Override   // Validation 예외가 발생했을 경우 잡는다
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, ex);
    }
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, BindException e) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponse(errorCode, e));
    }

    private ResultDto makeErrorResponse(ErrorCode errorCode, BindException e) {
        FieldError error = e.getFieldError() ;
        String msg = "" ;
        if (error != null) msg = error.getDefaultMessage() ;
        return ResultDto.builder()
                .status(errorCode.getHttpStatus())
                .code(FAILURE)
                .resultMsg(msg)
                .resultData(errorCode.name())   // errorCode.name() : enum의 이름
                // validation 에러 메세지를 정리하는 메서드
                .build();
    }

}