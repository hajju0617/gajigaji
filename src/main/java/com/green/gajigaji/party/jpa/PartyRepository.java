package com.green.gajigaji.party.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
    Party findPartyByPartySeq(Long partySeq);
}
