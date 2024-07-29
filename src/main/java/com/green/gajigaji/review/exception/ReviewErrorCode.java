package com.green.project2nd.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.green.project2nd.review.exception.ReviewConst.ERROR;
import static com.green.project2nd.review.exception.ReviewConst.FAILURE;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    DUPLICATED_REVIEW(HttpStatus.BAD_REQUEST, "리뷰는 한 번만 작성할 수 있습니다.", FAILURE),
    UNIDENTIFIED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "의도치않은 오류가 발생했습니다.", ERROR);

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
