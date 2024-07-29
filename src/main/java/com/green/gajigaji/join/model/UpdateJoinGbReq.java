package com.green.gajigaji.join.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateJoinGbReq {
    @JsonIgnore
    @Schema(example = "1", description = "모임 PK")
    private Long joinPartySeq;
    @Schema(example = "2", description = "유저 PK")
    private Long joinUserSeq;
    @Schema(example = "1", description = "모임장의 유저 PK")
    private Long leaderUserSeq;
    @Schema(example = "1", description = "신청문 상태")
    private int joinGb;
}
