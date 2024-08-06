package com.green.gajigaji.planjoin.jpa;

import com.green.gajigaji.common.jpa.UpdateDt;
import com.green.gajigaji.member.jpa.PartyMember;
import com.green.gajigaji.plan.jpa.PlanMaster;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@DynamicInsert
@DynamicUpdate
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
}
