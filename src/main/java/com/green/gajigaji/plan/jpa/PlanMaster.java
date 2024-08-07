package com.green.gajigaji.plan.jpa;

import com.green.gajigaji.common.jpa.UpdateDt;
import com.green.gajigaji.party.jpa.PartyMaster;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Setter
@Getter
@DynamicInsert
@DynamicUpdate
public class PlanMaster extends UpdateDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planSeq;

    @ManyToOne
    @JoinColumn(name = "plan_party_seq", nullable = false)
    private PartyMaster partyMaster;

    @Column(nullable = false)
    private String planStartDt;

    @Column(nullable = false)
    private String planStartTime;

    @ColumnDefault("1")
    @Column
    private String planCompleted;

    @Column(nullable = false)
    private String planTitle;

    @Column(nullable = false)
    private String planContents;

    @Column(nullable = false)
    private String planLocation;

}
