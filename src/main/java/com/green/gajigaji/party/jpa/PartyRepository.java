package com.green.gajigaji.party.jpa;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PartyRepository extends JpaRepository<PartyMaster, Long> {
    PartyMaster findPartyByPartySeq(Long partySeq);

    @Transactional
    @Modifying
    @Query("UPDATE PartyMaster p SET p.partyAuthGb = CASE " +
            "WHEN :num = 2 THEN 2 " +
            "WHEN :num = 3 THEN 3 " +
            "WHEN :num = 4 THEN 4 " +
            "ELSE p.partyAuthGb END " +
            "WHERE p.partySeq = :partySeq")
    int updatePartyAuthGb(Long partySeq, int num);
    boolean existsByPartySeq(Long partySeq);

}
