package com.green.gajigaji.common;

import com.green.gajigaji.planjoin.model.TogglePlanJoinReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CheckMapper {
    //범준
    int checkPartyLeader(Long partySeq, Long userSeq);

    int checkPartySeq(Long partySeq);
    int checkUserSeq(Long userSeq);
    int checkPartyName(String partyName, Long partySeq);

    int checkJoinApplicationOfUser(Long partySeq, Long userSeq);
    int checkMemberForPartySeqAndUserSeq(Long partySeq, Long userSeq);
    int checkPartyNowMem(Long partySeq);


    //예림
    Integer checkBudgetPartySeq(Long budgetPartySeq);
    Integer checkBudgetMemberSeq(Long memberPartySeq, Long memberSeq);
    Integer checkBudgetSeq(Long budgetSeq);
    Integer checkPlanSeq(Long planSeq);
    Integer checkPlanPartySeq(Long planPartySeq);
    Integer checkPlanJoin(TogglePlanJoinReq p);
    Integer checkPlanCompleted(long planSeq);

    //영록
    Integer checkPostedReview(long reviewPlanSeq, long reviewPlmemberSeq);
    Integer checkReview(long reviewSeq);
    Integer checkReviewPics(long reviewSeq);
    Integer checkMemberRole(long memberPartySeq, long memberUserSeq);
    Integer checkMemberUserSeqByMemberSeq(long memberSeq);
    Integer checkJoinApplicationForm(long joinPartySeq, long joinUserSeq);
    //Integer checkMemberExisted(long memberPartySeq, long memberUserSeq);
}
