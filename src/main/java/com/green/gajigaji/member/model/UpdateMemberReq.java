package com.green.gajigaji.member.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateMemberReq {
    @JsonIgnore
    @Schema(example = "1", description = "모임 PK")
    private Long memberPartySeq;
    @Schema(example = "1", description = "유저 PK")
    private Long memberUserSeq;
    @Schema(example = "2", description = "멤버역할")
    private int memberRole;

}
