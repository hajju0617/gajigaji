package com.green.gajigaji.partywish.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Setter
public class PartyWishToggleReq {

    @Schema(example = "1", description = "유저 PK 값")
    @JsonIgnore
    private long wishUserSeq;

    @Schema(example = "1", description = "모임 PK 값")
    private long wishPartySeq;
}
