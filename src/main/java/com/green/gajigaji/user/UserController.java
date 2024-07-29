package com.green.gajigaji.user;


import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.user.model.*;
import com.green.gajigaji.user.userexception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.green.gajigaji.user.userexception.ConstMessage.*;



@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User", description = "유저")
@RequestMapping("api/user")
public class UserController {
    private final UserService service;

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
    public ResultDto<Long> postSignUp(@RequestPart(value = "userPic") MultipartFile userPic, @Valid @RequestPart(value = "p") SignUpReq p) {

        try {
            long result = service.postSignUp(userPic, p);

            return ResultDto.<Long>builder()
                    .status(HttpStatus.OK)
                    .code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE)
                    .resultData(result)
                    .build();
        } catch (PwCheckException pe) {
            return ResultDto.<Long>builder().status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(PASSWORD_CHECK_MESSAGE)
                    .build();   // 비밀번호 확인 불일치
        } catch (DuplicationException de) {
            return ResultDto.<Long>builder().status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(EMAIL_DUPLICATION_MESSAGE)
                    .build();   // 이메일 중복
        } catch (BirthDateException be) {
            return ResultDto.<Long>builder().status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(BIRTHDATE_REGEX_MESSAGE)
                    .build();   // 생년월일 형식
        } catch (FileException fe) {
            return ResultDto.<Long>builder().status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(FILE_ERROR_MESSAGE)
                    .build();   // 파일 에러
        } catch (NumberDuplicationException ne) {
            return ResultDto.<Long>builder().status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(NUMBER_DUPLICATION_MESSAGE)
                    .build();   // 번호 중복
        } catch (RuntimeException r) {
            return ResultDto.<Long>builder().status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(NICKNAME_DUPLICATION_MESSAGE)
                    .build();   // 닉네임 중복
        } catch (Exception e) {
            return ResultDto.<Long>builder().status(HttpStatus.INTERNAL_SERVER_ERROR).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
//        } catch (EmailRegexException ee) {
//            return ResultDto.<Long>builder().status(HttpStatus.BAD_REQUEST).code(FAILURE)
//                    .resultMsg(EMAIL_REGEX_MESSAGE)
//                    .build();   // 이메일 형식
//        } catch (NicknameRegexException ne) {
//            return ResultDto.<Long>builder().status(HttpStatus.BAD_REQUEST).code(FAILURE)
//                    .resultMsg(NICKNAME_REGEX_MESSAGE)
//                    .build();   // 닉네임 형식
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
        try {
            SignInRes result = service.postSignIn(res, p);

            return ResultDto.<SignInRes>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
        } catch (LoginException le) {      // 아이디 or 비번 확인 or 비회원가입
            return ResultDto.<SignInRes>builder().status(HttpStatus.NOT_FOUND).code(FAILURE)
                    .resultMsg(le.getMessage()).build();
        } catch (Exception e) {
            return ResultDto.<SignInRes>builder().status(HttpStatus.BAD_REQUEST).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
    }
    @GetMapping("access-token")
    public ResultDto<Map<String, String>> getAccessToken(HttpServletRequest req) {
        Map<String, String> map = service.getAccessToken(req);

        return ResultDto.<Map<String, String>>builder()
                .code(1)
                .resultMsg("Access Token 발급")
                .resultData(map)
                .build();
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

        try {
            int result = service.patchPassword(p);
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
        } catch (PwCheckException pe) {
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(PASSWORD_CHECK_MESSAGE).build();
        } catch (Exception e) {
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
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
        try {
            int result = service.deleteUser();
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
        } catch (NotFoundException ne) {
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(ne.getMessage()).build();
        } catch (FileException fe) {
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).code(FAILURE)
                    .resultMsg(fe.getMessage()).build();
        } catch (Exception e) {
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
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
    public ResultDto<UserEntity> getDetailUserInfo() {
        try {
            UserEntity result = service.getDetailUserInfo();
            return ResultDto.<UserEntity>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
        } catch (RuntimeException re) {
            return ResultDto.<UserEntity>builder()
                    .status(HttpStatus.NOT_FOUND).code(FAILURE)
                    .resultMsg(FAILURE_MESSAGE).build();
        } catch (Exception e) {
            return ResultDto.<UserEntity>builder()
                    .status(HttpStatus.BAD_REQUEST).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
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
        try {
            int result = service.duplicatedCheck(str, num);
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(result == SUCCESS ? IS_DUPLICATE : IS_NOT_DUPLICATE).resultData(result)
                    .build();
        } catch (IllegalArgumentException ie) {
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.BAD_REQUEST).code(FAILURE)
                    .resultMsg(ie.getMessage()).build();
        } catch (Exception e) {
            return ResultDto.<Integer>builder().status(HttpStatus.BAD_REQUEST).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
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
        try {
            String result = service.updateUserPic(p);
            return ResultDto.<String>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result).build();
        } catch (FileException e) {
            return ResultDto.<String>builder().status(HttpStatus.INTERNAL_SERVER_ERROR).code(FAILURE)
                    .resultMsg(e.getMessage()).build();
        } catch (Exception e) {
            return ResultDto.<String>builder().status(HttpStatus.BAD_REQUEST).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
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
        try {
            int result = service.updateUserInfo(p);
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result)
                    .build();
        } catch (RuntimeException re) {
            re.printStackTrace();
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.NOT_FOUND).code(FAILURE)
                    .resultMsg(re.getMessage()).build();
        } catch (Exception e) {
            return ResultDto.<Integer>builder()
                    .status(HttpStatus.BAD_REQUEST).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
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
        try {
            String result = service.findUserId(p);
            return ResultDto.<String>builder()
                    .status(HttpStatus.OK).code(SUCCESS)
                    .resultMsg(SUCCESS_MESSAGE).resultData(result)
                    .build();
        } catch (RuntimeException re) {
            return ResultDto.<String>builder()
                    .status(HttpStatus.NOT_FOUND).code(FAILURE)
                    .resultMsg(re.getMessage()).build();
        } catch (Exception e) {
            return ResultDto.<String>builder()
                    .status(HttpStatus.BAD_REQUEST).code(ERROR)
                    .resultMsg(ADMIN_CONTACT_MESSAGE).build();
        }
    }
}
