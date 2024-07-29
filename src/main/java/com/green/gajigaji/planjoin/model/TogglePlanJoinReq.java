package com.green.gajigaji.planjoin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TogglePlanJoinReq {
    @Schema(example = "1", description = "모임 일정 마스터 PK 값")
    private Long plmemberPlanSeq;
    @Schema(example = "1", description = "모임 멤버 PK 값")
    private Long memberSeq;
}