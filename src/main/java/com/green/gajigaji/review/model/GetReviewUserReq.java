package com.green.gajigaji.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewUserReq {
    private int search;
    private String searchData;

    @JsonIgnore
    private long userSeq;

    private Integer page;
    private Integer size;
    private int startIdx;

    public GetReviewUserReq(Integer page, Integer size, int search, String searchData){
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
