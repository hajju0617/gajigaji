package com.green.gajigaji.planjoin;

import com.green.gajigaji.planjoin.model.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanJoinMapper {
    int postPlanJoin(TogglePlanJoinReq p);
    int deletePlanJoin(TogglePlanJoinReq p);

    GetMemberSeqRes getMemberSeq(long memberSeq);
}
