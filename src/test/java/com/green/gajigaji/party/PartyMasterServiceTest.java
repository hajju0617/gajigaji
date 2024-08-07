package com.green.gajigaji.party;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Import({ PartyServiceImpl.class })
class PartyMasterServiceTest {

    @MockBean
    PartyMapper mapper;
    @Autowired
    PartyService service;

    @Test
    @DisplayName("모임 생성 등록")
    void postParty() {




    }

    @Test
    void getPartyLocation() {
    }

    @Test
    void getParty() {
    }

    @Test
    void getPartyDetail() {
    }

    @Test
    void getPartyMine() {
    }

    @Test
    void getPartyLeader() {
    }

    @Test
    void updateParty() {
    }

    @Test
    void updatePartyAuthGb() {
    }

    @Test
    void updatePartyForGb2() {
    }
}