package com.green.gajigaji.party.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GetPartyPage {
    private List<GetPartyRes> list;
    private long totalPage;
    private long totalElements;

    public GetPartyPage(List<GetPartyRes> list, Integer size, long totalElements) {
        this.list = list;
        this.totalElements = totalElements;
        this.totalPage = (this.totalElements + size - 1) / size;
    }
}
