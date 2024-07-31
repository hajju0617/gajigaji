package com.green.gajigaji.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.green.gajigaji.common.GlobalConst.FAILURE;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    INCORRECT_ID_PW(HttpStatus.BAD_REQUEST, "아이디, 비밀번호를 확인해 주세요.", FAILURE),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.", FAILURE),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다.", FAILURE),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "로그인을 해주세요.", FAILURE),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.", FAILURE),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", FAILURE);
    // 생성자를 호출하면서 enum을 만듦 (HttpStatus.BAD_REQUEST -> httpStatus, "유효하지 않은 토큰입니다." -> message)


    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
