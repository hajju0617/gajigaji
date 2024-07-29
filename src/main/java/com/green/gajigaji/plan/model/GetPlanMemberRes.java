package com.green.gajigaji.plan.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetPlanMemberRes {
    private long planSeq;
    private long plmemberSeq;
    private long userSeq;
    private String userName;
}
