package com.green.gajigaji.user.model;

import lombok.Getter;

import java.util.List;

@Getter
public class SimpleInfo {

    private long userSeq;

    private String userEmail;

    private String userPw;

    private String userNickname;

    private String userPic;

    private String userName;
    private int userGender;
    private String userBirth;
    private String userGenderNm;

    private String userAddr;
    private String userPhone;
    private List<String> userRole;
}
