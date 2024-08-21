package com.green.gajigaji.party.exception;

import com.green.gajigaji.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.green.gajigaji.common.GlobalConst.FAILURE;

@Getter
@RequiredArgsConstructor
public enum PartyErrorMessage implements ErrorCode{
        NOT_FOUND_PARTY(HttpStatus.BAD_REQUEST, "존재하지 않는 모임입니다", FAILURE),
        NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저", FAILURE);

        private final HttpStatus httpStatus;
        private final String message;
        private final int code;
}
