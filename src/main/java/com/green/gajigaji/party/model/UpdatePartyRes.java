package com.green.gajigaji.party.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdatePartyRes {
    @Schema(example = "모임 사진", description = "사진 파일 이름")
    private String partyPic;
}
