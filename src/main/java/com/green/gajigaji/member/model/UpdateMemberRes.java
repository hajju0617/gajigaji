package com.green.gajigaji.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateMemberRes {
    @Schema(example = "1", description = "멤버 PK")
    private Long memberSeq;
    @Schema(example = "1", description = "멤버상태(0:비활성, 1:활성, 2:거부)")
    private int memberGb;
}
