package com.green.gajigaji.member.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostMemberReq {
    @JsonIgnore
    @Schema(example = "1", description = "멤버 PK")
    private Long memberSeq;
    @JsonIgnore
    @Schema(example = "1", description = "모임 PK")
    private Long memberPartySeq;
    @Schema(example = "1", description = "유저 PK")
    private Long memberUserSeq;
}
