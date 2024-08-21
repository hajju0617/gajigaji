package com.green.gajigaji.join.exception;

import com.green.gajigaji.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.green.gajigaji.common.GlobalConst.FAILURE;

@Getter
@RequiredArgsConstructor
public enum JoinErrorCode implements ErrorCode {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저", FAILURE),
    NOT_ALLOWED(HttpStatus.BAD_REQUEST, "권한이 없습니다.", FAILURE),
    NOT_YOUR_APPLICATION_FORM(HttpStatus.BAD_REQUEST, "본인의 신청서가 아닙니다.", FAILURE),
    BAD_REQUEST_JOIN_GB(HttpStatus.BAD_REQUEST, "신청 구분이 잘못되었습니다.(정상값 / 1:승인, 2:거절)", FAILURE);

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
