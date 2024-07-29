package com.green.gajigaji.board.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BoardDeleteReq {
    private long boardSeq;
    private long boardMemberSeq;
    private long boardPartySeq;
}
