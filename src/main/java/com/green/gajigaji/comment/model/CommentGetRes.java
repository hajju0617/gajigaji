package com.green.gajigaji.comment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentGetRes {
    private long commentSeq;
    private long commentBoardSeq;
    private long commentMemberSeq;
    private String userName;
    private String commentContents;
    private String inputDt;
    private String updateDt;
}
