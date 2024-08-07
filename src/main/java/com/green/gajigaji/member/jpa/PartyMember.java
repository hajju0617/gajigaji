package com.green.gajigaji.member.jpa;

import com.green.gajigaji.common.jpa.UpdateDt;
import com.green.gajigaji.party.jpa.PartyMaster;
import com.green.gajigaji.user.jpa.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { "member_user_seq", "member_party_seq" }
                )
        }
)
public class PartyMember extends UpdateDt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;

    @ManyToOne
    @JoinColumn(name = "member_user_seq", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "member_party_seq", nullable = false)
    private PartyMaster partyMaster;

    @Column(nullable = false)
    private String memberRole;

    @ColumnDefault("1")
    @Column(nullable = false)
    private int memberGb;


}
