package com.green.gajigaji.board.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardGetReq{
    @Schema(description = "모임 PK")
    private long boardPartySeq;

    @Schema(description = "페이지 번호 (0부터 시작)")
    private int page;

    @Schema(description = "페이지 크기")
    private int size;

    public BoardGetReq(long boardPartySeq, int page, int size) {
        this.boardPartySeq = boardPartySeq;
        this.page = Math.max(0, page);
        this.size = Math.max(1, size);


    }


}
