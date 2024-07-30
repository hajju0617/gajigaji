package com.green.gajigaji.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PostReviewReq{
    @JsonIgnore
    private long reviewSeq;

    @NotBlank(message = "일정 PK는 필수값입니다.")
    @Schema(example = "1", description = "일정 PK")
    private long reviewPlanSeq;

    @NotBlank(message = "일정 참가자 PK는 필수값입니다.")
    @Schema(example = "1", description = "일정 참가자 PK")
    private long reviewPlmemberSeq;

    @NotBlank(message = "리뷰 내용은 필수값입니다.")
    @Schema(example = "내용", description = "리뷰 내용")
    private String reviewContents;

    @NotBlank(message = "리뷰 별점은 필수값입니다.")
    @Pattern(regexp = "[1-5]", message = "리뷰 별점은 1~5 사이의 값이어야 합니다.")
    @Schema(example = "5", description = "별점")
    private int reviewRating;
}
