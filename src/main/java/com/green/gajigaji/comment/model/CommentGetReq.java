package com.green.gajigaji.comment.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString

public class CommentGetReq {
    private long boardSeq;
    private Integer size;
    private Integer page;
    private int startIdx;


    public CommentGetReq(Integer page, Integer size, long boardSeq) {
        this.page = page == null ? 1 : page;
        this.size = size == null ? 15 : size;
        this.startIdx = this.page - 1 < 0 ? 0 : (this.page - 1) * this.size;
        this.boardSeq = boardSeq;
    }
}


