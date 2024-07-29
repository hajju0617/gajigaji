package com.green.gajigaji.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordReq {
    @Schema(example = "abc123@naver.com", description = "유저 이메일")
    private String userEmail;

    @JsonIgnore
    private String userSetPw;
}
