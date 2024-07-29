package com.green.gajigaji.party.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetPartyReq2 {
    @Schema(example = "1", description = "유저 PK")
    private long userSeq;
    @Schema(example = "1", description = "확인할 페이지")
    private int page;
    @JsonIgnore
    @Schema(example = "9", description = "확인할 사이즈")
    private int size;

    public GetPartyReq2(Integer page, Integer size) {
        this.page = page == null ? 1 : page;
        this.size = size == null ? 9 : size;
        this.startIdx = (this.page - 1) * this.size;
    }
    @JsonIgnore
    private int startIdx;

}
