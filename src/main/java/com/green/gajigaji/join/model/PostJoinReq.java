package com.green.gajigaji.join.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostJoinReq {
    @JsonIgnore
    @Schema(example = "1", description = "신청문 PK")
    private Long joinSeq;
    @JsonIgnore
    @Schema(example = "1", description = "모임 PK")
    private Long joinPartySeq;
    @Schema(example = "2", description = "유저 PK")
    private Long joinUserSeq;
    @Schema(example = "00년생, 홍길동입니다.", description = "가입 신청문")
    private String joinMsg;
}
