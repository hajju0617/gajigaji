package com.green.gajigaji.party.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<PartyMaster, Long> {
    PartyMaster findPartyByPartySeq(Long partySeq);


}
