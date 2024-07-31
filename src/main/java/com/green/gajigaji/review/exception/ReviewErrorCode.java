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
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다.", FAILURE);


    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
