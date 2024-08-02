package com.green.gajigaji.planjoin.jpa;

import com.green.gajigaji.common.jpa.UpdateDt;
import com.green.gajigaji.member.jpa.PartyMember;
import com.green.gajigaji.plan.jpa.PlanMaster;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { "plmember_plan_seq", "plmember_member_seq" }
                )
        }
)
public class PlanMember extends UpdateDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plmemberSeq;

    @ManyToOne
    @JoinColumn(name = "plmember_plan_seq", nullable = false)
    private PlanMaster planMaster;

    @ManyToOne
    @JoinColumn(name = "plmember_member_seq", nullable = false)
    private PartyMember partyMember;

    @Column(nullable = false)
    private LocalDate planStartDt;

    @Column(nullable = false)
    private LocalDateTime planStartTime;

    @Column(nullable = false)
    private int planCompleted;

    @Column(nullable = false)
    private String planTitle;

    @Column(nullable = false)
    private String planContents;

    @Column(nullable = false)
    private String planLocation;
}
