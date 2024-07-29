package com.green.gajigaji.budget.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GetBudgetRes {
    private long budgetSeq;
    private long budgetPartySeq;
    private long budgetMemberSeq;
    private int budgetGb;
    private String cdNm;
    private int budgetAmount;
    private String budgetDt;
    private String budgetText;
    private String budgetPic;
}
