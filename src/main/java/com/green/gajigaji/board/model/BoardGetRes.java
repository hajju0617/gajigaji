package com.green.gajigaji.board.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class BoardGetRes {
    private long boardSeq;
    private long boardPartySeq;
    private long boardMemberSeq;
    private String userName;
    private String boardTitle;
    private String boardContents;
    private long boardHit;
    private LocalDateTime inputDt;
    private LocalDateTime updateDt;
    private List<String> pics;

}
