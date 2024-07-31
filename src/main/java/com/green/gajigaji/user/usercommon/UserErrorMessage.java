package com.green.gajigaji.user.usercommon;

import com.green.gajigaji.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static com.green.gajigaji.common.GlobalConst.*;

@Getter
@RequiredArgsConstructor
public enum UserErrorMessage implements ErrorCode {
    TRY_AGAIN_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "나중에 다시 시도해 주세요", ERROR),
    FILE_ERROR_MESSAGE(HttpStatus.BAD_REQUEST, "파일을 첨부하지 않았거나 처리 도중 오류발생", ERROR),
    NUMBER_DUPLICATION_MESSAGE(HttpStatus.CONFLICT, "이미 가입되어 있는 번호", FAILURE),
    PIC_INPUT_MESSAGE(HttpStatus.BAD_REQUEST, "프로필 사진은 필수값", FAILURE),
    NOT_FOUND_MESSAGE(HttpStatus.NOT_FOUND, "존재하지 않는 유저", FAILURE),
    LOGIN_MESSAGE(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호 확인", FAILURE),
    EMAIL_DUPLICATION_MESSAGE(HttpStatus.CONFLICT, "중복된 이메일", FAILURE),
    NICKNAME_DUPLICATION_MESSAGE(HttpStatus.CONFLICT, "중복된 닉네임", FAILURE),
    PASSWORD_CHECK_MESSAGE(HttpStatus.BAD_REQUEST, "비밀번호가 틀림", FAILURE),
    EMAIL_ALREADY_VERIFIED_MESSAGE(HttpStatus.CONFLICT, "이미 인증 된 이메일", FAILURE),
    UNREGISTERED_EMAIL_MESSAGE(HttpStatus.BAD_REQUEST, "가입되어 있지 않은 이메일", FAILURE),
    AUTH_CODE_EXPIRED(HttpStatus.NOT_FOUND, "인증번호 유효시간 만료", FAILURE),
    AUTH_CODE_INCORRECT(HttpStatus.BAD_REQUEST, "인증번호 틀림", FAILURE),
    INPUT_VALIDATION_MESSAGE(HttpStatus.BAD_REQUEST, "입력값을 제대로 입력해 주세요", FAILURE),
    IS_DUPLICATE(HttpStatus.CONFLICT, "중복입니다", FAILURE),
    BIRTHDATE_REGEX_MESSAGE(HttpStatus.BAD_REQUEST, "생년월일 형식 안 맞음", FAILURE),

    MISSING_REFRESH_TOKEN_MESSAGE(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 없거나 만료되었습니다.", FAILURE);

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

}
