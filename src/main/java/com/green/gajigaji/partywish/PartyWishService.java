package com.green.gajigaji.partywish;

import com.green.gajigaji.partywish.model.PartyWishGetListRes;
import com.green.gajigaji.partywish.model.PartyWishToggleReq;
import com.green.gajigaji.security.AuthenticationFacade;
import com.green.gajigaji.user.userexception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.green.gajigaji.user.usercommon.UserErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyWishService {
    private final PartyWishMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public int togglePartyWish(PartyWishToggleReq p) {

            int result = mapper.deletePartyWish(p);
            if(result == 1) {
                return 0;
            }

        return mapper.insertPartyWish(p);
    }

    public List<PartyWishGetListRes> partyWishGetList(long userSeq) {
            return mapper.partyWishGetList(userSeq);

    }
}
