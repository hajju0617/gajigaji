package com.green.gajigaji.admin;

import com.green.gajigaji.admin.model.UpdatePartyGb;
import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.party.exception.PartyErrorMessage;
import com.green.gajigaji.party.jpa.PartyMaster;
import com.green.gajigaji.party.jpa.PartyRepository;
import com.green.gajigaji.user.email.MailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.Part;


@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {
    private final AdminMapper mapper;
    private final MailSendService mailSendService;
    private final PartyRepository partyRepository;



    public int updatePartyAuthGb(UpdatePartyGb p) {
//        long userSeq = authenticationFacade.getLoginUserId();
        if(!partyRepository.existsByPartySeq(p.getPartySeq())) {
            throw new CustomException(PartyErrorMessage.NOT_FOUND_PARTY);
        }

//        if(mapper.existsParty(p.getPartySeq()) == 0) {
//            throw new CustomException(PartyErrorMessage.NOT_FOUND_PARTY);
//        }

        mailSendService.handlePartyRequest(p.getUserEmail(), p.getText());
        return partyRepository.updatePartyAuthGb(p.getPartySeq(), p.getNum());
    }
}
