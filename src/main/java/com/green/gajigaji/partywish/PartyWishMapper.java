package com.green.gajigaji.partywish;

import com.green.gajigaji.partywish.model.PartyWishGetListRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartyWishMapper {
    int insertPartyWish(long wishUserSeq, long wishPartySeq);

    int deletePartyWish(long wishUserSeq, long wishPartySeq);

    List<PartyWishGetListRes> partyWishGetList(long wishUserSeq);

}
