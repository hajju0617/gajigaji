package com.green.project2nd.review.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewFavToggleReq {
    private long reviewFavUserSeq;
    private long reviewFavReviewSeq;
}
