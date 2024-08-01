package com.green.gajigaji.budget.jpa;

import com.green.gajigaji.common.jpa.UpdateDt;
import com.green.gajigaji.member.jpa.Member;
import com.green.gajigaji.party.jpa.Party;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "partyBudget")
@Getter
@Setter
public class Budget extends UpdateDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetSeq;

    @ManyToOne
    @JoinColumn(name = "budget_party_seq", nullable = false)
    private Party party;

    @ManyToOne
    @JoinColumn(name = "budget_member_seq", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int budgetGb;

    @Column(nullable = false)
    private int budgetAmount;

    @Column(nullable = false)
    private LocalDateTime budgetDt;

    @Column
    private String budgetText;

    @Column
    private String budgetPic;
}
