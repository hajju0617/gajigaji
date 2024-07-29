package com.green.gajigaji.partywish.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PartyWishToggleReq {
    @Schema(example = "1", description = "유저 PK 값")
    private long wishUserSeq;
    @Schema(example = "1", description = "모임 PK 값")
    private long wishPartySeq;
}
