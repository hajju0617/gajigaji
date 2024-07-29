package com.green.gajigaji.party.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostPartyRes {
    @Schema(example = "모임 PK", description = "모임 PK")
    private long partySeq;
    @Schema(example = "모임 사진", description = "사진 파일 이름")
    private String partyPic;
}
