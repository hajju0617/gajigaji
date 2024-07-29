package com.green.gajigaji.budget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PatchBudgetReq {
    @Schema(example = "1", description = "모임 회계 PK 값")
    private Long budgetSeq;

    @Schema(example = "1", description = "모임 멤버 PK 값")
    private Long budgetMemberSeq;

    @Schema(example = "1(입금) / 2(출금)", description = "(선택) 입금 or 출금 여부")
    private Integer budgetGb;

    @Schema(example = "50000", description = "(선택) 금액")
    private Integer budgetAmount;

    @Schema(example = "24-07-23", description = "(선택) 입출금 날짜")
    private String budgetDt;

    @Schema(example = "회비 입금", description = "(선택) 입출금 상세 내역")
    private String budgetText;

    @JsonIgnore private String budgetPic;
}
