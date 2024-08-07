package com.green.gajigaji.member.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<PartyMember, Long> {
    PartyMember findPartyMemberByMemberSeq(Long memberSeq);
}
