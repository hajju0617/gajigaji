package com.green.gajigaji.party.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetPartyRes {
    @Schema(example = "1", description = "모임 PK")private long partySeq;
    @Schema(example = "축구 모임", description = "모임 이름")private String partyName;
    @Schema(example = "스포츠", description = "모임 분야")private String partyGenre;
    @Schema(example = "0101", description = "모임 지역")private String partyLocation;
    @Schema(example = "서울", description = "모임 지역 시")private String partyLocation1;
    @Schema(example = "강남구", description = "모임 지역 구")private String partyLocation2;
    @Schema(example = "2010", description = "최소년도")private int partyMinAge;
    @Schema(example = "1990", description = "최대년도")private int partyMaxAge;
    @Schema(example = "1", description = "모임 성별")private int partyGender;
    @Schema(example = "1", description = "멤버모집상태")private int partyJoinGb;
    //스웨거 보내기용
    @Schema(example = "10", description = "현재 인원수")private int partyNowMem;
    @Schema(example = "100", description = "최대 인원수")private int partyMaximum;
    @Schema(example = "축구 하실분", description = "모임소개")private String partyIntro;
    @Schema(example = "나이와 자기소개 작성필수", description = "가입양식")private String partyJoinForm;
    @Schema(example = "1", description = "모임상태")private String partyAuthGb;
    @Schema(example = "사진 url용 주소", description = "모임사진")private String partyPic;
    @Schema(example = "등록일자", description = "등록일자")private String inputDt;
    @Schema(example = "수정일자", description = "수정일자")private String updateDt;



    @Schema(example = "1", description = "모임장 유저 PK")private long userSeq;
    @Schema(example = "김모임장", description = "모임장 이름")private String userName;
    @Schema(example = "사진 url용 주소", description = "모임장 사진")private String userPic;


}
