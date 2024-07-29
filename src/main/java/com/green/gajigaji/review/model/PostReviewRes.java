package com.green.project2nd.review.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostReviewRes {
    private long reviewSeq;
    private List<String> pics;
}
