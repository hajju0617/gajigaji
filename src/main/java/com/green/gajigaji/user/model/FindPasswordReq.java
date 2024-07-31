package com.green.gajigaji.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static com.green.gajigaji.user.usercommon.UserErrorMessage.INPUT_VALIDATION_MESSAGE;
import static com.green.gajigaji.user.usercommon.UserMessage.EMAIL_INPUT_MESSAGE;
import static com.green.gajigaji.user.usercommon.UserMessage.ID_INPUT_MESSAGE;

@Getter
@Setter
public class FindPasswordReq {
    @Schema(example = "abc123@naver.com", description = "유저 이메일")
    @NotBlank(message = EMAIL_INPUT_MESSAGE)
    private String userEmail;

    @JsonIgnore
    private String userSetPw;
}
