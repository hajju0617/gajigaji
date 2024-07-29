package com.green.gajigaji.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInRes {
    @Schema(example = "1", description = "유저 PK 값")
    private long userSeq;
    private String userEmail;

    @Schema(example = "가나다", description = "유저 이름")
    private String userName;
    @Schema(example = "닉네임123", description = "유저 닉네임")
    private String userNickname;

    private String userAddr;
    @Schema(example = "2000-01-01", description = "유저 생년월일")
    private String userBirth;

    @Schema(example = "남자(2) / 여자(1)", description = "유저 성별")
    private int userGender;
    private String userGenderNm;

    private String userPhone;
    @Schema(example = "asdfqwer123.jpg", description = "유저 프로필 사진")
    private String userPic;


    private String accessToken;
}
