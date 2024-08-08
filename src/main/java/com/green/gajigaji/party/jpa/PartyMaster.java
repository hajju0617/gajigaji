package com.green.gajigaji.party.jpa;

import com.green.gajigaji.admin.model.UpdatePartyGb;
import com.green.gajigaji.common.jpa.UpdateDt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Year;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class PartyMaster extends UpdateDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partySeq;

    @Column(unique = true, nullable = false)
    private String partyName;

    @Column(nullable = false)
    private int partyGenre;

    @Column(nullable = false)
    private String partyLocation;

    @Column(nullable = false)
    private Year partyMinAge;

    @Column(nullable = false)
    private Year partyMaxAge;

    @ColumnDefault("100")
    @Column(nullable = false)
    private int partyMaximum;

    @Column(nullable = false)
    private int partyGender;

    @Column(nullable = false)
    private int partyJoinGb;

    @Column(nullable = false)
    private String partyIntro;

    @Column(nullable = false)
    private String partyJoinForm;

    @ColumnDefault("1")
    @Column(nullable = false)
    private int partyAuthGb;

    @Column(nullable = false)
    private String partyPic;

    public PartyMaster(UpdatePartyGb p) {
        this.setPartySeq(p.getPartySeq());
        this.setPartyAuthGb(p.getNum());
    }

}
