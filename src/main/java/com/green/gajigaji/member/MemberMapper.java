package com.green.gajigaji.member;

import com.green.gajigaji.member.model.GetMemberRes;
import com.green.gajigaji.member.model.UpdateMemberReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<GetMemberRes> getMember(Long memberPartySeq);
    GetMemberRes getMemberDetail(Long memberPartySeq, Long memberUserSeq);
    int updateMember(UpdateMemberReq p);
    int updateMemberGb(long memberSeq);
}
