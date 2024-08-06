package com.green.gajigaji.common.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "commonCd")
@Getter
@Setter
public class CommonCd extends UpdateDt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdSeq;

    @Column(nullable = false)
    private String cdMain;

    @Column(nullable = false)
    private String cdSub;

    @Column(nullable = false)
    private String cd;

    @Column(nullable = false)
    private String cdGb;

    @Column
    private String cdGbNm;

}
