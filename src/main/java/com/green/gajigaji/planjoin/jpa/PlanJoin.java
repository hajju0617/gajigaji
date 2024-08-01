package com.green.gajigaji.planjoin.jpa;

import com.green.gajigaji.member.jpa.Member;
import com.green.gajigaji.member.jpa.UpdateDt;
import com.green.gajigaji.plan.jpa.Plan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "planMember")
@Setter
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { "plmember_plan_seq", "plmember_member_seq" }
                )
        }
)
public class PlanJoin extends UpdateDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plmemberSeq;

    @ManyToOne
    @JoinColumn(name = "plmember_plan_seq", nullable = false)
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "plmember_member_seq", nullable = false)
    private Member member;

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
