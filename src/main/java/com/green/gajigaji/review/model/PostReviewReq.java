package com.green.project2nd.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostReviewReq{
    @JsonIgnore
    private long reviewSeq;
    @Schema(example = "1", description = "일정 PK")
    private long reviewPlanSeq;
    @Schema(example = "1", description = "일정 참가자 PK")
    private long reviewPlmemberSeq;
    @Schema(example = "내용", description = "리뷰 내용")
    private String reviewContents;
    @Schema(example = "5", description = "별점")
    private int reviewRating;
}
