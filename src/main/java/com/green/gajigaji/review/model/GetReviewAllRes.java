package com.green.project2nd.review.model;

import lombok.Data;

import java.util.List;

@Data
public class GetReviewAllRes {
    private long reviewSeq;
    private long reviewPlanSeq;
    private long reviewPlmemberSeq;
    private long userSeq;
    private String userPic;
    private String userName;
    private String reviewContents;
    private String reviewRating;
    private String inputDt;
    private long partySeq;
    private String partyName;
    private String president;
    private int favCnt;

    private List<String> pics;
}