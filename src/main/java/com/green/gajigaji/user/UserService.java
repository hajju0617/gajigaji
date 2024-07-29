package com.green.gajigaji.user;


import com.green.gajigaji.common.model.AppProperties;
import com.green.gajigaji.common.model.CookieUtils;
import com.green.gajigaji.common.model.CustomFileUtils;
import com.green.gajigaji.security.AuthenticationFacade;
import com.green.gajigaji.security.MyUser;
import com.green.gajigaji.security.MyUserDetails;
import com.green.gajigaji.security.jwt.JwtTokenProviderV2;
import com.green.gajigaji.user.datacheck.CommonUser;
import com.green.gajigaji.user.model.*;
import com.green.gajigaji.user.userexception.*;
import com.green.gajigaji.user.userexception.DuplicationException;
import com.green.gajigaji.user.userexception.LoginException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static com.green.gajigaji.user.userexception.ConstMessage.*;



@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private int i;
    private final UserMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Long postSignUp(MultipartFile userPic, SignUpReq p) throws Exception {
        if(userPic == null || userPic.isEmpty()) {
            throw new FileException(PIC_INPUT_MESSAGE);
        }
        if(!p.getUserPw().equals(p.getUserPwCheck())) {
            throw new PwCheckException(PASSWORD_CHECK_MESSAGE);
        }
        if(CommonUser.isValidDate(p.getUserBirth())) {
            CommonUser.convertToDate(p.getUserBirth());
        } else {
            throw new BirthDateException(BIRTHDATE_REGEX_MESSAGE);
        }
        if(mapper.duplicatedCheckEmail(p.getUserEmail()) == 1) {
            throw new DuplicationException(EMAIL_DUPLICATION_MESSAGE);
        }
        if(mapper.duplicatedCheckNumber(p.getUserPhone()) == 1) {
            throw new NumberDuplicationException(NUMBER_DUPLICATION_MESSAGE);
        }
        if(mapper.duplicatedCheckNickname(p.getUserNickname()) == 1) {
            throw new RuntimeException(NICKNAME_DUPLICATION_MESSAGE);
        }
//        if(!isValidEmail(p.getUserEmail())) {
//            throw new EmailRegexException(EMAIL_REGEX_MESSAGE);
//        }
//        if(!isValidNickname(p.getUserNickname())) {
//            throw new NicknameRegexException(NICKNAME_REGEX_MESSAGE);
//        }

        String saveFileName = customFileUtils.makeRandomFileName(userPic);
        p.setUserPic(saveFileName);
        String hashPw = passwordEncoder.encode(p.getUserPw());
        p.setUserPw(hashPw);

        int result = mapper.postSignUp(p);

        try {
            String path = String.format("user/%d", p.getUserSeq());
            customFileUtils.makeFolders(path);
            String target = String.format("%s/%s", path, saveFileName);
            customFileUtils.transferTo(userPic, target);

        } catch (FileException fe) {
            fe.printStackTrace();
            throw new FileException(FILE_ERROR_MESSAGE); //구체적 예외처리 하기
        }
        return p.getUserSeq();
    }

    public SignInRes postSignIn(HttpServletResponse res, SignInReq p) {
        SimpleInfo user = mapper.getSimpleUserInfo(p.getUserEmail());

        if(user == null || !(p.getUserEmail().equals(user.getUserEmail())) || !(passwordEncoder.matches(p.getUserPw(), user.getUserPw()))) {
            throw new LoginException(LOGIN_MESSAGE);
        }

        MyUser myUser = MyUser.builder()
                .userId(user.getUserSeq())
                .role("ROLE_USER")
                .build();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        log.info("appProperties.getJwt().getRefreshTokenCookieName() : {}", appProperties.getJwt().getRefreshTokenCookieName());
        cookieUtils.setCookie(res, appProperties.getJwt().getRefreshTokenCookieName(), refreshToken, refreshTokenMaxAge);

        return SignInRes.builder()
                .userNickname(user.getUserNickname())
                .userPic(user.getUserPic())
                .userSeq(user.getUserSeq())
                .userBirth(user.getUserBirth())
                .userName(user.getUserName())
                .userGender(user.getUserGender())
                .userEmail(user.getUserEmail())
                .userAddr(user.getUserAddr())
                .userPhone(user.getUserPhone())
                .userGenderNm(user.getUserGenderNm())
                .accessToken(accessToken)
                .build();
    }
    public Map getAccessToken(HttpServletRequest req) {
        Cookie cookie = cookieUtils.getCookie(req, appProperties.getJwt().getRefreshTokenCookieName());
        if(cookie == null) { // refresh-token 값이 쿠키에 존재 여부
            throw new RuntimeException();
        }

        String refreshToken = cookie.getValue();
        if(!jwtTokenProvider.isValidateToken(refreshToken)) { //refresh-token 만료시간 체크
            throw new RuntimeException();
        }

        UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);
        MyUser myUser = ((MyUserDetails)auth).getMyUser();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);

        Map map = new HashMap();
        map.put("accessToken", accessToken);
        return map;
    }

    public int patchPassword(UpdatePasswordReq p) {
        p.setUserSeq(authenticationFacade.getLoginUserId());
//        UserEntity user = mapper.getDetailUserInfo(p.getUserSeq());
        String userPw = mapper.getUserPw(p.getUserSeq());


        if (!(passwordEncoder.matches(p.getUserPw(), userPw)) || !(p.getUserNewPw().equals(p.getUserPwCheck()))) {
            throw new PwCheckException(PASSWORD_CHECK_MESSAGE);
        }
        String newPassword = passwordEncoder.encode(p.getUserNewPw());
        p.setUserNewPw(newPassword);
//        p.setUserSeq(user.getUserSeq());
        return mapper.patchPassword(p);
    }

    @Transactional
    public int deleteUser() {
        long userPk = authenticationFacade.getLoginUserId();
        int result = mapper.userExists(userPk);
        if(result == 0) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        try {
            String midPath = String.format("user/%d", userPk);
            String delAbsoluteFolderPath = String.format("%s%s", customFileUtils.uploadPath, midPath);
            customFileUtils.deleteFolder(delAbsoluteFolderPath);

        } catch (FileException fe) {
            throw new FileException(FILE_ERROR_MESSAGE);
        }
        return mapper.deleteUser(userPk);
    }

    public UserEntity getDetailUserInfo() {
        long userPk = authenticationFacade.getLoginUserId();
        UserEntity userEntity = mapper.getDetailUserInfo(userPk);
        if(userEntity == null) {
            throw new RuntimeException(FAILURE_MESSAGE);
        }
        return userEntity;
    }

    public int duplicatedCheck(String str, int num) {   // 1 : 이메일, 2 : 닉네임
        switch (num) {
            case 1 -> num = mapper.duplicatedCheckEmail(str);
            case 2 -> num = mapper.duplicatedCheckNickname(str);
            default -> throw new IllegalArgumentException(INPUT_VALIDATION_MESSAGE);
        }
        return num;
    }

    @Transactional
    public String updateUserPic(UpdateUserPicReq p) throws Exception {
        long userPk = authenticationFacade.getLoginUserId();

        String fileName = customFileUtils.makeRandomFileName(p.getPic());
        p.setPicName(fileName);
        mapper.updateUserPic(p);

        try {
            String Path = String.format("user/%d", userPk);
            String delAbsoluteFolderPath = String.format("%s%s", customFileUtils.uploadPath, Path);
            customFileUtils.deleteFolder(delAbsoluteFolderPath);

            customFileUtils.makeFolders(Path);
            String filePath = String.format("%s/%s", Path, fileName);
            customFileUtils.transferTo(p.getPic(), filePath);

        } catch (FileException fe) {
            throw new FileException(FILE_ERROR_MESSAGE);  // 에러 처리하기
        }
        return fileName;
    }

    public int updateUserInfo(UpdateUserInfoReq p) {
        long userPk = authenticationFacade.getLoginUserId();

        int result = mapper.updateUserInfo(p.getUserNickname(), p.getUserAddr(), p.getUserFav(), p.getUserPhone(), p.getUserIntro(), userPk);
        if(result == 0) {
            throw new RuntimeException(FAILURE_MESSAGE);
        }
        return result;
    }

    public String findUserId(FindUserReq p) {
        String userEmail = mapper.findUserId(p);
        if(userEmail == null) {
            throw new RuntimeException(NOT_FOUND_MESSAGE);
        }
        return CommonUser.maskEmail(userEmail);
    }
}
