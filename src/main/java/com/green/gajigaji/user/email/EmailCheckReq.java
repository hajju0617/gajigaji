package com.green.gajigaji.user.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailCheckReq {

    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String userEmail;

    @NotEmpty(message = "인증 번호를 입력해 주세요")
    private String authNum;

}
