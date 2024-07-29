package com.green.gajigaji.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@Builder
public class BoardPostRes {
    private long boardSeq;
    private long boardPartySeq;
    private long boardMemberSeq;
    private String boardTitle;
    private String boardContents;
    private long boardHit;
    private String inputDt;
    private String updateDt;

    private List<String> pics;
}
