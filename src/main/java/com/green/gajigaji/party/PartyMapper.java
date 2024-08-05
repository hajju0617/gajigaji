package com.green.gajigaji.party;

import com.green.gajigaji.party.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartyMapper {
    int postParty(PostPartyReq p);
    int postMemberForPostParty(PostPartyReq p);

    List<GetPartyLocationRes> getPartyLocation(int cdSub, int cdGb);
    List<GetPartyLocationRes> getPartyLocationAll(int cdSub);
    List<GetPartyRes> getParty();
    GetPartyRes getPartyDetail(Long partySeq);

    List<GetPartyRes2List> getPartyMine(GetPartyReq2 p);
    int getPartyMineCount(long userSeq);
    List<GetPartyRes2List> getPartyLeader(GetPartyReq2 p);
    int getPartyLeaderCount(long userSeq);

    int updateParty(UpdatePartyReq p);
    int updatePartyAuthGb1(Long partySeq, Long userSeq);
    void updatePartyAuthGb2(Long partySeq);

}
