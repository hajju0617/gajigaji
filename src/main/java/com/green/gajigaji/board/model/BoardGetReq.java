package com.green.gajigaji.board.model;

import com.green.gajigaji.common.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BoardGetReq extends Paging {
    private long boardSeq;
    private long boardMemberSeq;
    private long boardPartySeq;

    public BoardGetReq(long boardSeq, Integer page, Integer size) {
        super(page, size);
        this.boardSeq = boardSeq;
    }

}
