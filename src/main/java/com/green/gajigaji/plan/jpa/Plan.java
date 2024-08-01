package com.green.gajigaji.plan.jpa;

import com.green.gajigaji.party.jpa.Party;
import com.green.gajigaji.party.jpa.UpdateDt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "planMaster")
@Setter
@Getter
public class Plan extends UpdateDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planSeq;

    @ManyToOne
    @JoinColumn(name = "plan_party_seq", nullable = false)
    private Party party;

    @Column(nullable = false)
    private String  planStartDt;

    @Column(nullable = false)
    private String planStartTime;

    @ColumnDefault("0")
    @Column(nullable = true)
    private int planCompleted;

    @Column(nullable = false)
    private String planTitle;

    @Column(nullable = false)
    private String planContents;

    @Column(nullable = false)
    private String planLocation;


}
