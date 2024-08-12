package com.green.gajigaji.review.model;

import lombok.Data;

import java.util.List;

@Data
public class GetReviewPartyPageRes {//미사용중
    private long totalElements;
    private long totalPages;
    private List<GetReviewPartyRes> list;

    public GetReviewPartyPageRes(long totalElements, int size, List<GetReviewPartyRes> list) {
        this.totalElements = totalElements;
        if(size != 0) {
            this.totalPages = (this.totalElements + size - 1) / size;
        } else {
            this.totalPages = 1;
        }
        this.list = list;
    }
}
