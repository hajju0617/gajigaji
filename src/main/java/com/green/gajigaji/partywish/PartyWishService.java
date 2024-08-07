package com.green.gajigaji.partywish;

import com.green.gajigaji.partywish.model.PartyWishGetListRes;

import com.green.gajigaji.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class PartyWishService {
    private final PartyWishMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public int togglePartyWish(long wishPartySeq) {
        long wishUserSeq = authenticationFacade.getLoginUserId();
        int result = mapper.deletePartyWish(wishUserSeq, wishPartySeq);
        if(result == 1) {
            return 0;
        }
        return mapper.insertPartyWish(wishUserSeq, wishPartySeq);
    }

    public List<PartyWishGetListRes> partyWishGetList() {
        long wishUserSeq = authenticationFacade.getLoginUserId();
        return mapper.partyWishGetList(wishUserSeq);

    }
}
