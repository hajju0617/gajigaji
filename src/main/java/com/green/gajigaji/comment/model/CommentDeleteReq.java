package com.green.gajigaji.comment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDeleteReq {
    private Long commentSeq;
    private Long commentMemberSeq;
}
