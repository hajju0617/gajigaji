package com.green.project2nd.review.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetReviewAllReq{
    private Integer search;
    private String searchData;
    private Integer page;
    private Integer size;
    private int startIdx;

    public GetReviewAllReq(Integer page, Integer size, Integer search, String searchData) {
        this.page = page;
        this.size = size;
        if(page != 0 && size != 0) {
            this.startIdx = this.page - 1 < 0 ? 0 : (this.page - 1) * this.size;
        } else {
            startIdx = 0;
        }
        this.search = search;
        this.searchData = searchData;
    }
}