package com.green.gajigaji.join.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetJoinRes {
    @Schema(example = "1", description = "신청서 PK")
    private Long joinSeq;
    @Schema(example = "00홍길동", description = "유저 이름")
    private String userName;
    @Schema(example = "ran123.jpg", description = "유저 사진")
    private String userPic;
    @Schema(example = "1", description = "유저 성별")
    private int userGender;
    @Schema(example = "2", description = "유저 PK")
    private Long joinUserSeq;
    @Schema(example = "00년생 홍길동입니다. 가입 신청합니다.", description = "가입신청문")
    private String joinMsg;
    @Schema(example = "0", description = "신청문 상태(0:미처리, 1:승인, 2:거부)")
    private int joinGb;


}
