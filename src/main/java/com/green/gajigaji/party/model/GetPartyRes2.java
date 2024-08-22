package com.green.gajigaji.party.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class GetPartyRes2 {
    @Schema(example = "10", description = "총 레코드 수") private final int TotalElements;
    @Schema(example = "2", description = "총 페이지 수") private final int TotalPages ;


    public GetPartyRes2(int TotalElements, int TotalPages , List<GetPartyRes2List> list) {
        this.TotalElements = TotalElements;
        this.TotalPages = TotalPages;
        this.list = list;
    }
    @Schema(example = "모임 데이터값", description = "모임 데이터값")
    private final List<GetPartyRes2List> list;



}
