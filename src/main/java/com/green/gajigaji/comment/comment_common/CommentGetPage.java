package com.green.gajigaji.comment.comment_common;

import com.green.gajigaji.comment.model.CommentGetRes;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class CommentGetPage {
    private List<CommentGetRes> list;
    private long totalPage;
    private long totalElements;
    public CommentGetPage(List<CommentGetRes> list, Integer size, long totalElements) {
        this.list = list;
        this.totalElements = totalElements;
        this.totalPage = (this.totalElements + size - 1) / size;
    }
}
