package com.green.project2nd.review.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class PostReviewPicDto {
    private long reviewSeq;
    @Builder.Default
    private List<String> fileNames = new ArrayList();
}
