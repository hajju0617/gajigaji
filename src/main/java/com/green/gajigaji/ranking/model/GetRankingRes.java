package com.green.gajigaji.ranking.model;

import lombok.Data;

@Data
public class GetRankingRes {
    private long partySeq;
    private String partyName;
    private String president;
    private String presidentPic;
    private int partyGenre;
    private String partyGenreNm;
    private int partyLocation;
    private String partyLocationNm;
    private int totalPoints;
}
