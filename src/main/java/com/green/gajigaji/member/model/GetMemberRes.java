package com.green.gajigaji.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetMemberRes {
    @Schema(example = "1", description = "멤버 PK")
    private Long memberSeq;
    @Schema(example = "홍길동", description = "유저 이름")
    private String userName;
    @Schema(example = "ran123.jpg", description = "유저 사진")
    private String userPic;
    @Schema(example = "1", description = "유저 성별")
    private int userGender;

    @Schema(example = "1", description = "유저 PK")
    private Long memberUserSeq;
    @Schema(example = "1", description = "멤버역할(1:모임장, 2:모임원)")
    private String memberRole;
    @Schema(example = "1", description = "멤버상태(0:비활성, 1:활성, 2:거부)")
    private int memberGb;
}
