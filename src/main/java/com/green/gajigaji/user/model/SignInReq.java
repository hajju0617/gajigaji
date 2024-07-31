package com.green.gajigaji.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


import static com.green.gajigaji.user.usercommon.UserMessage.ID_INPUT_MESSAGE;
import static com.green.gajigaji.user.usercommon.UserMessage.PW_INPUT_MESSAGE;

@Getter
@Setter
public class SignInReq {

    @Schema(example = "abc123@naver.com", description = "유저 이메일")
    @NotBlank(message = ID_INPUT_MESSAGE)
    private String userEmail;

    @Schema(example = "abcd1234!", description = "유저 비밀번호")
    @NotBlank(message = PW_INPUT_MESSAGE)
    private String userPw;
}
