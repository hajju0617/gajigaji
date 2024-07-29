package com.green.gajigaji.comment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentPostReq {
    //@JsonIgnore
    //private long comment_seq;

    private long commentBoardSeq;
    private long commentMemberSeq;
    private String commentContents;
}
