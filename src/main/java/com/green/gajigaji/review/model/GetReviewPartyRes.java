package com.green.gajigaji.review.model;

import lombok.Data;

import java.util.List;

@Data
public class GetReviewPartyRes {
    private long partySeq;
    private String partyName;
    private String planContents;
    private long reviewSeq;
    private String reviewContents;
    private int reviewRating;
	private int favCnt;
	private long userSeq;
    private String userName;
	private String userPic;
	private String inputDt;

    private List<String> pics;
}
