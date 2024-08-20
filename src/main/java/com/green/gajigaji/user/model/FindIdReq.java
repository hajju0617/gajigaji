package com.green.gajigaji.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FindIdReq {
    @Schema(example = "가나다", description = "유저 이름")
    private String userName;

    @Schema(example = "01012345678", description = "유저 전화번호")
    private String userPhone;

    @Schema(example = "2024-01-01", description = "유저 생년월일")
    private String userBirth;
}
