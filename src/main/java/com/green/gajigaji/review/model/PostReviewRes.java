package com.green.gajigaji.review.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostReviewRes {
    private long reviewSeq;
    private List<String> pics;
}
