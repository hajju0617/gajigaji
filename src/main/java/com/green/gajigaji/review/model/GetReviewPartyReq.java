package com.green.gajigaji.review.model;

import lombok.Data;

@Data
public class GetReviewPartyReq {
    private long partySeq;
    private Integer page;
    private Integer size;
    private int startIdx;

    public GetReviewPartyReq(Integer page, Integer size, long partySeq) {
        this.partySeq = partySeq;
        this.page = page;
        this.size = size;
        if (page != 0 && size != 0) {
            this.startIdx = this.page - 1 < 0 ? 0 : (this.page - 1) * this.size;
        } else {
            startIdx = 0;
        }
    }
}
