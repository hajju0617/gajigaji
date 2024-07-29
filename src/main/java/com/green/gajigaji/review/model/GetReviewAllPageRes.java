package com.green.gajigaji.review.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Getter
@Setter
@ToString
@Slf4j
public class GetReviewAllPageRes {
    private long totalElements;
    private long totalPages;
    private List<GetReviewAllRes> list;

    public GetReviewAllPageRes(long totalElements, int size, List<GetReviewAllRes> list) {
        this.totalElements = totalElements;
        if(size != 0) {
            this.totalPages = (this.totalElements + size - 1) / size;
        } else {
            this.totalPages = 1;
        }
        this.list = list;
    }
}
