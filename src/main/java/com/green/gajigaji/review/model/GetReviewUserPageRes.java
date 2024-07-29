package com.green.gajigaji.review.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class GetReviewUserPageRes {
    private long totalElements;
    private long totalPages;
    private List<GetReviewUserRes> list;

    public GetReviewUserPageRes(long totalElements, int size, List<GetReviewUserRes> list) {
        this.totalElements = totalElements;
        if(size != 0) {
            this.totalPages = (this.totalElements + size - 1) / size;
        } else {
            this.totalPages = 1;
        }
        this.list = list;
    }
}