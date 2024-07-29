package com.green.gajigaji.plan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostPlanReq {
    @JsonIgnore private long planSeq;

    @Schema(example = "1", description = "모임 마스터 PK 값")
    private Long planPartySeq;

    @Schema(example = "24-07-23", description = "모임 일정 시작 날짜")
    private String planStartDt;

    @Schema(example = "12:00", description = "모임 일정 시작 시간")
    private String planStartTime;

    @Schema(example = "대구광역시 중구 1길", description = "(선택) 모임 장소")
    private String planLocation;

    @Schema(example = "정기 모임", description = "일정 제목")
    private String planTitle;

    @Schema(example = "단체 회식", description = "일정 내용")
    private String planContents;

}
