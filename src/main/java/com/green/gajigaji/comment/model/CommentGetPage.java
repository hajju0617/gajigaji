package com.green.gajigaji.comment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class CommentGetPage {
    private List<CommentGetRes> commentList;
    private long totalPage;
    private long totalElements;

    public CommentGetPage(List<CommentGetRes> list, Integer size, long totalElements) {
        this.commentList = list;
        this.totalElements = totalElements;
        this.totalPage = (this.totalElements + size - 1) / size;
    }
}
