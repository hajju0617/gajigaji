package com.green.gajigaji.plan.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetPlanRes {
    private long planSeq;
    private String planStartDt;
    private String planStartTime;
    private int planCompleted;
    private String cdNm;
    private String planTitle;
    private String planContents;
    private String planLocation;
}
