package com.green.gajigaji.user.email;


import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.user.model.FindPasswordReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.green.gajigaji.common.GlobalConst.SUCCESS;
import static com.green.gajigaji.common.GlobalConst.SUCCESS_MESSAGE;
import static com.green.gajigaji.user.usercommon.UserErrorMessage.*;



@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "email", description = "email 작업")
public class MailController {
    private final MailSendService mailService;

    @PostMapping("/mailSend")
    @Operation(summary = "이메일 발송" , description =
    "<strong > 이메일 발송(본인 인증용) </strong> <p></p>" +
            "<p><strong> userEmail</strong> : 유저 이메일 (String) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공  </p> " +
                            "<p>  2 : 실패  </p> " +
                            "<p>  3 : 에러 "
    )
    public ResultDto<String> mailSend(@RequestBody @Valid EmailRequestReq emailDto) {
        log.info("이메일 인증 이메일 : {}", emailDto.getUserEmail());
            String str = mailService.joinEmail(emailDto.getUserEmail());

            return ResultDto.<String>builder().status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(str).build();


    }


    @PostMapping("/mailauthCheck")
    @Operation(summary = "이메일로 전송된 임시번호 확인" , description =
    "<strong > 이메일로 전송된 임시번호 확인 </strong> <p></p>" +
            "<p><strong> userEmail</strong> : 유저 이메일 (String) </p>" +
            "<p><strong> authNum</strong> : 인증번호 (String) </p>"
            )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공  </p> " +
                            "<p>  2 : 실패  </p> " +
                            "<p>  3 : 에러 "
    )
    public ResultDto<String> AuthCheck(@RequestBody @Valid EmailCheckReq p){   // 이메일이랑 임의 코드
        String checked = mailService.CheckAuthNum(p.getUserEmail(),p.getAuthNum());
        log.info("Check : {}", checked);
        log.info("p.getUserEmail() : {}", p.getUserEmail());
        log.info("p.getAuthNum() : {}", p.getAuthNum());

        return ResultDto.<String>builder()
                .status(HttpStatus.OK).code(SUCCESS)
                .resultMsg(SUCCESS_MESSAGE).resultData(checked).build();


    }

    @PatchMapping("/findpw")
    @Operation(summary = "이메일로 임시비밀번호 발급" , description =
    "<strong > 이메일로 임시비밀번호 발급 </strong> <p></p>" +
            "<p><strong> userEmail</strong> : 유저 이메일 (String) </p>"
            )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공  </p> " +
                            "<p>  2 : 실패  </p> " +
                            "<p>  3 : 에러 "
    )
    public ResultDto<String> setPassword(@RequestBody FindPasswordReq p) {
            String result = mailService.setPassword(p);

            return ResultDto.<String>builder()
                    .status(HttpStatus.OK)
                    .code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE)
                    .resultData(result)
                    .build();

    }
}
