package com.green.gajigaji.member.exception;

import com.green.gajigaji.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.green.gajigaji.common.GlobalConst.ERROR;
import static com.green.gajigaji.common.GlobalConst.FAILURE;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    NOT_JOINED_USER(HttpStatus.BAD_REQUEST, "해당 모임에 가입하지 않은 유저입니다.", FAILURE),
    NOT_ALLOWED_TO_PRESIDENT(HttpStatus.BAD_REQUEST, "모임장은 탈퇴할 수 없습니다.", FAILURE),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저", FAILURE),
    ALREADY_LEFT_USER(HttpStatus.BAD_REQUEST, "이미 탈퇴한 유저입니다.", FAILURE),
    NOT_MATCHED_USER(HttpStatus.BAD_REQUEST, "로그인한 유저 본인이 아닙니다.", FAILURE);


    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
