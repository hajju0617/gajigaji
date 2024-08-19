package com.green.gajigaji.comment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class CommentGetReq  {
    private long boardSeq;
    private Integer size;
    private Integer page;
    private int startIdx;




    //   private int startIdx;

    public CommentGetReq(Integer page, Integer size, long boardSeq) {
        this.page = page;
        this.size = size;
        if(page != 0 && size != 0) {
            this.startIdx = this.page - 1 < 0 ? 0 : (this.page - 1) * this.size;
        } else {
            startIdx = 0;
        }
        this.boardSeq = boardSeq;
    }



    //  public CommentGetReq(long boardSeq, Integer page, Integer size) {
     //   super(page, size);
     //   this.boardSeq = boardSeq;
    //}
}


