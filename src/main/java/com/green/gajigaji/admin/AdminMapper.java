package com.green.gajigaji.admin;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    int handlePartyRequest(Long partySeq, int num);

    int existsParty(Long partySeq);
}
