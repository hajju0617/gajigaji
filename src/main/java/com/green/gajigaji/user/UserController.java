package com.green.gajigaji.user;


import com.green.gajigaji.common.model.AppProperties;
import com.green.gajigaji.common.model.CookieUtils;
import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.green.gajigaji.common.GlobalConst.*;

import static com.green.gajigaji.user.usercommon.UserMessage.IS_NOT_DUPLICATE;


@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User", description = "유저")
@RequestMapping("api/user")
public class UserController {
    private final UserService service;
    private final CookieUtils cookieUtils;
    private final AppProperties appProperties;

    @PostMapping("/sign_up")
    @Operation(summary = "회원가입" , description =
            "<strong > 유저 회원가입 </strong> <p></p>" +
            "<p><strong> userEmail</strong> : 유저 이메일 (long) </p>" +
            "<p><strong> userPw</strong> : 유저 비밀번호 (String) </p>" +
            "<p><strong> userPwCheck</strong> : 유저 비밀번호 확인(String) </p>" +
            "<p><strong> userName</strong> : 유저 이름(String) </p>" +
            "<p><strong> userAddr</strong> : 유저 주소(String) </p>" +
            "<p><strong> userNickname</strong> : 유저 닉네임(String) </p>" +
            "<p><strong> userFav</strong> : 유저 관심 모임(String) (NULL 허용) </p>" +
            "<p><strong> userBirth</strong> : 유저 생년월일(String) </p>" +
            "<p><strong> userGender</strong> : 유저 성별(int) </p>" +
            "<p><strong> userPhone</strong> : 유저 전화번호(String)  </p>" +
            "<p><strong> userIntro</strong> : 유저 자기소개(String) (NULL 허용) </p>" +
            "<p><strong> userPic</strong> : 유저 프로필 사진(String) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p> 1 : 성공 -> 유저 PK </p> " +
                            "<p> 2 : 실패 -> 비밀번호 확인 불일치 </p> " +
                            "<p>   : 이메일 형식이 안 맞음</p> " +
                            "<p>   : 닉네임 형식이 안 맞음</p> " +
                            "<p>   : 중복된 이메일</p> " +
                            "<p>   : 생년월일 형식 안 맞음</p> " +
                            "<p>   : 파일을 첨부하지 않았거나 처리 도중 오류발생</p> " +
                            "<p>   : 중복된 닉네임</p> " +
                            "<p> 3 : 관리자에게 문의하세요</p> "
    )
    public ResultDto<Long> postSignUp(@RequestPart(value = "userPic") MultipartFile userPic, @Valid @RequestPart(value = "p") SignUpReq p,  @Nullable Integer a) {

            long result = service.postSignUp(userPic, p, a);

            return ResultDto.<Long>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result)
                    .build();
    }

    @PostMapping("/sign_in")
    @Operation(summary = "로그인" , description =
            "<strong > 유저 로그인 </strong> <p></p>" +
            "<p><strong> userEmail</strong> : 유저 이메일 (long) </p>" +
            "<p><strong> userPw</strong> : 유저 비밀번호 (String) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공 -> 유저 PK, 유저 닉네임, 유저 프로필 사진, 유저 성별, 유저 나이, 유저 이름, 이메일,주소,전화번호, 토큰 </p> " +
                            "<p>  2 : 실패 -> (비회원가입 or 아이디 틀림 or 비밀번호 틀림) </p> " +
                            "<p>  3 : 에러 "
    )
    public ResultDto<SignInRes> postSignIn(HttpServletResponse res, @RequestBody SignInReq p) {
            SignInRes result = service.postSignIn(res, p);

            return ResultDto.<SignInRes>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
    }
    @GetMapping("access-token")
    public ResultDto<Map<String, String>> getAccessToken(HttpServletRequest req) {
        Map<String, String> map = service.getAccessToken(req);

        return ResultDto.<Map<String, String>>builder()
                .code(SUCCESS).resultMsg("Access Token 발급")
                .resultData(map).build();
    }

    @PatchMapping("/update/pw")
    @Operation(summary = "비밀번호 변경" , description =
            "<strong > 유저 비밀번호 변경 </strong> <p></p>" +
            "<p><strong> userEmail</strong> : 유저 이메일 (long) </p>" +
            "<p><strong> userPw</strong> : 유저 비밀번호 (String) </p>" +
            "<p><strong> userNewPw</strong> : 유저 새로운 비밀번호 (String) </p>" +
            "<p><strong> userPwCheck</strong> : 유저 새로운 비밀번호 확인 (String) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공 </p> " +
                            "<p>  2 : 실패 (아이디 틀림) </p> " +
                            "<p>  2 : 실패 (비밀번호 틀림) </p> " +
                            "<p>  3 : 에러 </p> "
    )
    public ResultDto<Integer> patchPassword(@Valid @RequestBody UpdatePasswordReq p) {
        int result = service.patchPassword(p);
        return ResultDto.<Integer>builder()
                .status(HttpStatus.OK).code(SUCCESS)
                .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
    }

    @PatchMapping()
    @Operation(summary = "유저 탈퇴" , description =
            "<strong > 유저 탈퇴 </strong> <p></p>" +
            "<p><strong> userEmail</strong> : 유저 이메일 (long) </p>" +
            "<p><strong> userPw</strong> : 유저 비밀번호 (String) </p>" +
            "<p><strong> userNewPw</strong> : 유저 새로운 비밀번호 (String) </p>" +
            "<p><strong> userPwCheck</strong> : 유저 새로운 비밀번호 확인 (String) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공 </p> " +
                            "<p>  2 : 실패 </p> " +
                            "<p>  3 : 에러 </p> "
    )
    public ResultDto<Integer> deleteUser() {
        int result = service.deleteUser();
        return ResultDto.<Integer>builder()
                .status(HttpStatus.OK).code(SUCCESS)
                .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
    }

    @GetMapping()
    @Operation(summary = "마이페이지" , description =
            "<strong > 유저 마이페이지 </strong> <p></p>" +
            "<p><strong> userSeq</strong> : 유저 PK (long) </p>" +
            "<p><strong> userEmail</strong> : 유저 이메일 (String) </p>" +
            "<p><strong> userName</strong> : 유저 이름 (String) </p>" +
            "<p><strong> userNickname</strong> : 유저 닉네임 (String) </p>" +
            "<p><strong> userAddr</strong> : 유저 주소 (String) </p>" +
            "<p><strong> userFav</strong> : 유저 관심목록 (String) </p>" +
            "<p><strong> userBirth</strong> : 유저 생년월일 (String) </p>" +
            "<p><strong> userGender</strong> : 유저 성별 (int) </p>" +
            "<p><strong> userPhone</strong> : 유저 전화번호 (String) </p>" +
            "<p><strong> userIntro</strong> : 유저 자기소개 (String) </p>" +
            "<p><strong> userGb</strong> : 유저 이메일 인증상태 (int) </p>" +
            "<p><strong> userPic</strong> : 유저 프로필 사진 (String) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공 </p> " +
                            "<p>  2 : 실패 </p> " +
                            "<p>  3 : 에러 </p> "
    )
    public ResultDto<UserDetails> getDetailUserInfo() {
        UserDetails result = service.getDetailUserInfo();
        return ResultDto.<UserDetails>builder()
                .status(HttpStatus.OK).code(SUCCESS)
                .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
    }

    @GetMapping("/duplicated")
    @Operation(summary = "이메일, 닉네임 중복체크" , description =
    "<strong > 이메일, 닉네임 중복체크 (1 : 이메일, 2 : 닉네임) </strong> <p></p>" +
            "<p><strong> str</strong> : 체크할 이메일 or 닉네임 (long) </p>" +
            "<p><strong> num</strong> : 1 : 이메일, 2 : 닉네임 (MultipartFile) </p>"
            )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공 -> 중복 입니다 or 중복이 아님 </p> " +
                            "<p>  2 : 실패 (입력값을 제대로 입력) </p> " +
                            "<p>  3 : 에러 </p> "
    )
    public ResultDto<Integer> duplicatedCheck(String str, int num) {
        log.info("str : {}", str);
        int result = service.duplicatedCheck(str, num);
        return ResultDto.<Integer>builder()
                .status(HttpStatus.OK).code(SUCCESS).resultMsg(IS_NOT_DUPLICATE)
                .resultData(result).build();
    }


    @PatchMapping(value = "pic", consumes = "multipart/form-data")
    @Operation(summary = "유저 프로필 사진 변경" , description =
            "<strong > 유저 프로필 사진 변경 </strong> <p></p>" +
            "<p><strong> userSeq</strong> : 유저 PK (long) </p>" +
            "<p><strong> pic</strong> : 유저 프로필 사진 (MultipartFile) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공 -> 변경된 사진 파일명 </p> " +
                            "<p>  2 : 실패 </p> " +
                            "<p>  3 : 에러 </p> "
    )
    public ResultDto<String> updateUserPic(@ModelAttribute UpdateUserPicReq p) {
            String result = service.updateUserPic(p);
            return ResultDto.<String>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
    }

    @PatchMapping("/update/myInfo")
    @Operation(summary = "유저 정보 수정" , description =
            "<strong > 유저 정보 수정 </strong> <p></p>" +
            "<p><strong> userNickname</strong> : 유저 닉네임 (String) </p>" +
            "<p><strong> userAddr</strong> : 유저 주소 (String) </p>" +
            "<p><strong> userFav</strong> : 유저 관심모임 (String) (NULL 허용) </p>" +
            "<p><strong> userPhone</strong> : 유저 전화번호 (String) </p>" +
            "<p><strong> userIntro</strong> : 유저 자기소개 (String) (NULL 허용) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공 </p> " +
                            "<p>  2 : 실패 </p> " +
                            "<p>  3 : 에러 </p> "
    )
    public ResultDto<Integer> updateUserInfo(@Valid @RequestBody UpdateUserInfoReq p) {
            int result = service.updateUserInfo(p);
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result)
                    .build();

    }

    @PostMapping("/findid")
    @Operation(summary = "아이디 찾기" , description =
            "<strong > 유저 아이디 찾기 </strong> <p></p>" +
            "<p><strong> userName</strong> : 유저 이름 (String) </p>" +
            "<p><strong> userPhone</strong> : 유저 전화번호 (String) </p>" +
            "<p><strong> userBirth</strong> : 유저 생년월일 (String) </p>"
    )
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p> " +
                            "<p>  1 : 성공 -> 유저 이메일 </p> " +
                            "<p>  2 : 실패 -> (비회원가입 or 아이디 틀림 or 비밀번호 틀림) </p> " +
                            "<p>  3 : 에러 </p> "
    )
    public ResultDto<String> findUserId(@RequestBody FindUserReq p) {
            String result = service.findUserId(p);
            return ResultDto.<String>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result)
                    .build();
    }

    @DeleteMapping("/logout")
    public ResultDto<Integer> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        int result = service.logout(httpServletRequest, httpServletResponse);
        return ResultDto.<Integer>builder()
                .status(HttpStatus.OK).code(SUCCESS)
                .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
    }
}
