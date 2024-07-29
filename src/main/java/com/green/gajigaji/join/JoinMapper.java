package com.green.gajigaji.join;

import com.green.gajigaji.join.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JoinMapper {
    int postJoin(PostJoinReq p);

    List<GetJoinRes> getJoin(Long joinPartySeq);
    GetJoinRes getJoinDetail(Long joinPartySeq, Long joinUserSeq);

    void updateJoin(UpdateJoinReq p);
    int updateJoinGb(UpdateJoinGbReq p);
    void updateSuspendedMember(Long joinPartySeq, Long joinUserSeq);
    void postMember(Long joinPartySeq, Long joinUserSeq);

    int deleteJoin(Long joinPartySeq, Long joinUserSeq);

    List<GetMyJoinRes> getMyJoin(long userSeq);

}
