package com.green.gajigaji.party.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Year;

@Entity(name = "partyMaster")
@Getter
@Setter
public class Party extends UpdateDt{
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
    private int PartyAuthGb;

    @Column(nullable = false)
    private String PartyPic;


}
