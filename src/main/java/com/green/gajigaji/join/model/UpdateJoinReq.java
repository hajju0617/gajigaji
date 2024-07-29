package com.green.gajigaji.join.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateJoinReq {
    @JsonIgnore
    @Schema(example = "1", description = "모임 PK")
    private Long joinPartySeq;
    @Schema(example = "1", description = "유저 PK")
    private Long joinUserSeq;
    @Schema(example = "01년생,홍길동입니다.", description = "가입신청문")
    private String joinMsg;
}
