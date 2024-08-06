package com.green.gajigaji.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewFavToggleReq {
    @JsonIgnore
    private long reviewFavUserSeq;

    private long reviewFavReviewSeq;
}
