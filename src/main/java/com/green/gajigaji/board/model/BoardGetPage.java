package com.green.gajigaji.board.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter

public class BoardGetPage {
    private List<BoardGetRes> list;
    private long totalPage;
    private long totalElements;

    public BoardGetPage(List<BoardGetRes> list, Integer size, long totalElements) {
        this.list = list;
        this.totalElements = totalElements;
        this.totalPage = (this.totalElements + size - 1) / size;
    }
}
