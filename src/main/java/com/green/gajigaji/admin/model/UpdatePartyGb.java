package com.green.gajigaji.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePartyGb {

    @Schema(example = "1", description = "모임 PK")
    private long partySeq;

    @Schema(example = "1", description = "모임 상태값")
    private int num;

    @Schema(example = "abc123@naver.com", description = "유저 이메일")
    private String userEmail;

    @Schema(example = "축하합니다 모임이 생성 되었습니다.", description = "메일에 넣을 내용")
    private String text;

}
