package com.green.gajigaji.join.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateJoinReq {
    @JsonIgnore
    private Long joinPartySeq;
    @JsonIgnore
    private Long joinUserSeq;
    @Schema(example = "01년생,홍길동입니다.", description = "가입신청문")
    private String joinMsg;
}
