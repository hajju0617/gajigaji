package com.green.gajigaji.member.jpa;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<PartyMember, Long> {
    PartyMember findPartyMemberByMemberSeq(Long memberSeq);


    @Modifying
    @Transactional
    @Query(value = "UPDATE party_member pm " +
            "JOIN user_master um ON pm.member_user_seq = um.user_seq " +
            "SET pm.member_gb = 2 " +
            "WHERE pm.member_user_seq = :userSeq", nativeQuery = true)
    void updatePartyMemberGb(@Param("userSeq") Long userSeq);
}
