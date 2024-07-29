package com.green.project2nd.review.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewUserReq {
    private int search;
    private String searchData;
    private long userSeq;
    private Integer page;
    private Integer size;
    private int startIdx;

    public GetReviewUserReq(Integer page, Integer size, int search, long userSeq, String searchData){
        this.page = page;
        this.size = size;
        if(page != 0 && size != 0) {
            this.startIdx = this.page - 1 < 0 ? 0 : (this.page - 1) * this.size;
        } else {
            startIdx = 0;
        }
        this.search = search;
        this.userSeq = userSeq;
        this.searchData = searchData;
    }
}
