package com.green.gajigaji.plan;

import com.green.gajigaji.plan.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanMapper {
    int postPlan(PostPlanReq p);
    int patchPlan(PatchPlanReq p);
    int patchPlanCompleted(long planSeq);
    List<GetPlanRes> getPlanAll(long planPartySeq);
    GetPlanRes getPlan(long planSeq);
    List<GetPlanMemberRes> getPlanMember(long planSeq);
    int deletePlan(long planSeq);
}
