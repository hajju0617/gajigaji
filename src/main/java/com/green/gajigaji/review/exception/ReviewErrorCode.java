package com.green.gajigaji.review.exception;

import com.green.gajigaji.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.green.gajigaji.common.GlobalConst.ERROR;
import static com.green.gajigaji.common.GlobalConst.FAILURE;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    DUPLICATED_REVIEW(HttpStatus.BAD_REQUEST, "리뷰는 한 번만 작성할 수 있습니다.", FAILURE),
    REVIEW_POST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰 등록 중 오류가 발생했습니다.", ERROR),
    REVIEW_PATCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰 수정 중 오류가 발생했습니다.", ERROR),
    REVIEW_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰 삭제 중 오류가 발생했습니다.", ERROR),
    REVIEW_PICS_POST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰 사진 등록 중 오류가 발생했습니다.", ERROR),
    UNIDENTIFIED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "의도치않은 오류가 발생했습니다.", ERROR),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다.", FAILURE),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저", FAILURE),
    NOT_POSTED_USER(HttpStatus.BAD_REQUEST, "해당 리뷰를 작성한 유저가 아닙니다.", FAILURE),
    NOT_JOINED_USER(HttpStatus.BAD_REQUEST, "해당 일정에 참가한 유저가 아닙니다.", FAILURE);


    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
