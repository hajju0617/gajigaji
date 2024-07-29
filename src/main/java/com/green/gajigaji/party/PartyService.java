package com.green.gajigaji.party;

import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.party.model.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface PartyService {

    ResultDto<PostPartyRes> postParty(@Nullable MultipartFile partyPic, PostPartyReq p) throws Exception;
    ResultDto<List<GetPartyLocationRes>> getPartyLocation(int cdSub, int cdGb);
    ResultDto<List<GetPartyRes>> getParty();
    ResultDto<GetPartyRes> getPartyDetail(Long partySeq);
    ResultDto<GetPartyRes2> getPartyMine(GetPartyReq2 p);
    ResultDto<GetPartyRes2> getPartyLeader(GetPartyReq2 p);
    ResultDto<UpdatePartyRes> updateParty(@Nullable MultipartFile partyPic, UpdatePartyReq p) throws Exception;
    ResultDto<Integer> updatePartyAuthGb1(Long partySeq, Long userSeq);
    ResultDto<Integer> updatePartyAuthGb2(Long partySeq, Long userSeq);


}
