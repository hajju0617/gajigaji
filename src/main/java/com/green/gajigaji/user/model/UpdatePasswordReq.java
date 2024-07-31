package com.green.gajigaji.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.green.gajigaji.user.usercommon.UserMessage.PASSWORD_REGEX_MESSAGE;
import static com.green.gajigaji.user.usercommon.UserMessage.PW_INPUT_MESSAGE;

@Getter
@Setter
public class UpdatePasswordReq {
//    @Schema(example = "abc123@naver.com", description = "유저 이메일")
//    private String userEmail;
    @Schema(example = "abcd1234!", description = "유저 비밀번호")
    @NotBlank(message = PW_INPUT_MESSAGE)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])\\S{10,20}$", message = PASSWORD_REGEX_MESSAGE)
    private String userPw;

    @Schema(example = "1234abcd!", description = "유저 새 비밀번호")
    @NotBlank(message = PW_INPUT_MESSAGE)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])\\S{10,20}$", message = PASSWORD_REGEX_MESSAGE)
    private String userNewPw;

    @Schema(example = "1234abcd!", description = "유저 새 비밀번호 확인")
    @NotBlank(message = PW_INPUT_MESSAGE)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])\\S{10,20}$", message = PASSWORD_REGEX_MESSAGE)
    private String userPwCheck;


    @JsonIgnore
    private long userSeq;
}
