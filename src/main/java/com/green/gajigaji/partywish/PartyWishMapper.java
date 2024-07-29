package com.green.gajigaji.partywish;

import com.green.gajigaji.partywish.model.PartyWishGetListRes;
import com.green.gajigaji.partywish.model.PartyWishToggleReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartyWishMapper {
    int insertPartyWish(PartyWishToggleReq p);

    int deletePartyWish(PartyWishToggleReq p);

    List<PartyWishGetListRes> partyWishGetList(long userSeq);

}
