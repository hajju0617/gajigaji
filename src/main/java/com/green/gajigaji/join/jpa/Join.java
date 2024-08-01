package com.green.gajigaji.join.jpa;

import com.green.gajigaji.party.jpa.Party;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "PartyJoin")
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { "join_party_seq", "join_user_seq" }
                )
        }
)

public class Join extends UpdateDt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long JoinSeq;

    @ManyToOne
    @JoinColumn(name = "join_party_seq", nullable = false)
    private Party party;

    @ManyToOne
    @JoinColumn(name = "join_user_seq", nullable = false)
    private User user;

    @Column(nullable = false)
    private String joinMsg;

    @Column(nullable = false)
    private int joinGb;

}
