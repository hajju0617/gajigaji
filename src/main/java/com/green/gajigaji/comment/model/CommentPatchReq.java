package com.green.gajigaji.comment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentPatchReq {
    private long commentSeq;
    private long commentMemberSeq;
    private String commentContents;

}
