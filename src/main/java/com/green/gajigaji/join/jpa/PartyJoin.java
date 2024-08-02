package com.green.gajigaji.join.jpa;

import com.green.gajigaji.common.jpa.UpdateDt;
import com.green.gajigaji.party.jpa.PartyMaster;
import com.green.gajigaji.user.jpa.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { "join_party_seq", "join_user_seq" }
                )
        }
)

public class PartyJoin extends UpdateDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long JoinSeq;

    @ManyToOne
    @JoinColumn(name = "join_party_seq", nullable = false)
    private PartyMaster partyMaster;

    @ManyToOne
    @JoinColumn(name = "join_user_seq", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String joinMsg;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int joinGb;

}
