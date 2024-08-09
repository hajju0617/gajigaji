package com.green.gajigaji.user;


import com.green.gajigaji.common.exception.CommonErrorCode;
import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.common.model.AppProperties;
import com.green.gajigaji.common.model.CookieUtils;
import com.green.gajigaji.common.model.CustomFileUtils;
import com.green.gajigaji.security.AuthenticationFacade;
import com.green.gajigaji.security.MyUser;
import com.green.gajigaji.security.MyUserDetails;
import com.green.gajigaji.security.SignInProviderType;
import com.green.gajigaji.security.jwt.JwtTokenProviderV2;
import com.green.gajigaji.user.jpa.UserEntity;
import com.green.gajigaji.user.usercommon.CommonUser;
import com.green.gajigaji.user.model.*;
import com.green.gajigaji.user.usercommon.UserErrorMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static com.green.gajigaji.common.GlobalConst.*;
import static com.green.gajigaji.user.usercommon.UserErrorMessage.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Transactional
    public Long postSignUp(MultipartFile userPic, SignUpReq p, Integer a) {
        if(userPic == null || userPic.isEmpty()) {
            throw new CustomException(PIC_INPUT_MESSAGE);
        }
        if(!p.getUserPw().equals(p.getUserPwCheck())) {
            throw new CustomException(PASSWORD_CHECK_MESSAGE);
        }
        if(CommonUser.isValidDate(p.getUserBirth())) {
            CommonUser.convertToDate(p.getUserBirth());
        } else {
            throw new CustomException(BIRTHDATE_REGEX_MESSAGE);
        }
        if(mapper.emailExists(p.getUserEmail()) == 1) {
            throw new CustomException(EMAIL_DUPLICATION_MESSAGE);
        }
        if(mapper.duplicatedCheckNumber(p.getUserPhone()) == 1) {
            throw new CustomException(NUMBER_DUPLICATION_MESSAGE);
        }
        if(mapper.duplicatedCheckNickname(p.getUserNickname()) == 1) {
            throw new CustomException(NICKNAME_DUPLICATION_MESSAGE);
        }
        String saveFileName = customFileUtils.makeRandomFileName(userPic);
        p.setUserPic(saveFileName);
        String hashPw = passwordEncoder.encode(p.getUserPw());
        p.setUserPw(hashPw);



        UserEntity userEntity = new UserEntity(p);
        userEntity.setUserBirth(CommonUser.convertToDate(p.getUserBirth()));
        userEntity.setUserPic(saveFileName);
        userEntity.setProviderType(SignInProviderType.LOCAL);
        if(a == null){
            userEntity.setUserRole("ROLE_USER");
        } else {
            userEntity.setUserRole("ROLE_ADMIN");
        }
        userRepository.save(userEntity);

//        int result = mapper.postSignUp(p);

        try {
            String path = String.format("user/%d", userEntity.getUserSeq());
            customFileUtils.makeFolders(path);
            String target = String.format("%s/%s", path, saveFileName);
            customFileUtils.transferTo(userPic, target);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR); //구체적 예외처리 하기
        }
        return userEntity.getUserSeq();
    }

    public SignInRes postSignIn(HttpServletResponse res, SignInReq p) {
        SimpleInfo userInfo = mapper.getSimpleUserInfo(p.getUserEmail());

        if(userInfo == null || !(p.getUserEmail().equals(userInfo.getUserEmail())) || !(passwordEncoder.matches(p.getUserPw(), userInfo.getUserPw()))) {
            throw new CustomException(INCORRECT_ID_PW);
        }

        MyUser myUser = MyUser.builder()
                .userId(userInfo.getUserSeq())
                .role(userInfo.getUserRole())
                .build();
        log.info("userInfo.getUserRole() : {}", userInfo.getUserRole());

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        log.info("appProperties.getJwt().getRefreshTokenCookieName() : {}", appProperties.getJwt().getRefreshTokenCookieName());
        cookieUtils.setCookie(res, appProperties.getJwt().getRefreshTokenCookieName(), refreshToken, refreshTokenMaxAge);

        return SignInRes.builder()                      // 프론트쪽 요청
                .userNickname(userInfo.getUserNickname())
                .userPic(userInfo.getUserPic())
                .userSeq(userInfo.getUserSeq())
                .userBirth(userInfo.getUserBirth())
                .userName(userInfo.getUserName())
                .userGender(userInfo.getUserGender())
                .userEmail(userInfo.getUserEmail())
                .userAddr(userInfo.getUserAddr())
                .userPhone(userInfo.getUserPhone())
                .userGenderNm(userInfo.getUserGenderNm())
                .accessToken(accessToken)
                .userRole(userInfo.getUserRole())
                .build();
    }

    public Map getAccessToken(HttpServletRequest req) {
        Cookie cookie = cookieUtils.getCookie(req, appProperties.getJwt().getRefreshTokenCookieName());
        if(cookie == null) { // refresh-token 값이 쿠키에 존재 여부
            throw new CustomException(MISSING_REFRESH_TOKEN_MESSAGE);
        }

        String refreshToken = cookie.getValue();
        if(!jwtTokenProvider.isValidateToken(refreshToken)) { //refresh-token 만료시간 체크
            throw new CustomException(EXPIRED_TOKEN);
        }

        org.springframework.security.core.userdetails.UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);


        MyUser myUser = ((MyUserDetails)auth).getMyUser();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);

        Map map = new HashMap<>();
        map.put("accessToken", accessToken);
        return map;
    }

    public int patchPassword(UpdatePasswordReq p) {
        p.setUserSeq(authenticationFacade.getLoginUserId());
//        UserEntity user = mapper.getDetailUserInfo(p.getUserSeq());
        String userPw = mapper.getUserPw(p.getUserSeq());



        if (!(passwordEncoder.matches(p.getUserPw(), userPw)) || !(p.getUserNewPw().equals(p.getUserPwCheck()))) {
            throw new CustomException(PASSWORD_CHECK_MESSAGE);
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
            throw new CustomException(NOT_FOUND_MESSAGE);
        }
        try {
            String midPath = String.format("user/%d", userPk);
            String delAbsoluteFolderPath = String.format("%s%s", customFileUtils.uploadPath, midPath);
            customFileUtils.deleteFolder(delAbsoluteFolderPath);

        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
        return mapper.deleteUser(userPk);
    }

    public UserDetails getDetailUserInfo() {
        long userPk = authenticationFacade.getLoginUserId();
        UserDetails userDetails = mapper.getDetailUserInfo(userPk);
        log.info("userEntity : {}", userDetails);

        return userDetails;
    }

    public int duplicatedCheck(String str, int num) {   // 1 : 이메일, 2 : 닉네임
        switch (num) {
            case 1 -> num = mapper.emailExists(str);
            case 2 -> num = mapper.duplicatedCheckNickname(str);
            default -> throw new CustomException(INPUT_VALIDATION_MESSAGE);
        }
        if(num == SUCCESS) {
            throw new CustomException(IS_DUPLICATE);
        }
        return num;
    }

    @Transactional
    public String updateUserPic(UpdateUserPicReq p) {
        long userPk = authenticationFacade.getLoginUserId();

        String fileName = customFileUtils.makeRandomFileName(p.getPic());
        p.setPicName(fileName);
        p.setUserSeq(userPk);

        mapper.updateUserPic(p);

        try {
            String Path = String.format("user/%d", userPk);
            String delAbsoluteFolderPath = String.format("%s%s", customFileUtils.uploadPath, Path);
            customFileUtils.deleteFolder(delAbsoluteFolderPath);

            customFileUtils.makeFolders(Path);
            String filePath = String.format("%s/%s", Path, fileName);
            customFileUtils.transferTo(p.getPic(), filePath);

        } catch (Exception fe) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
        return fileName;
    }

    public int updateUserInfo(UpdateUserInfoReq p) {
        long userPk = authenticationFacade.getLoginUserId();
        p.setUserSeq(userPk);

        int result = mapper.updateUserInfo(p);
        if(result == 0) {
            throw new CustomException(TRY_AGAIN_MESSAGE);
        }
        return result;
    }

    public String findUserId(FindUserReq p) {
        String userEmail = mapper.findUserId(p);
        if(userEmail == null) {
            throw new CustomException(NOT_FOUND_MESSAGE);
        }
        return CommonUser.maskEmail(userEmail);
    }

    public int logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie token = cookieUtils.getCookie(httpServletRequest, appProperties.getJwt().getRefreshTokenCookieName());
        if(token == null) {
            throw new CustomException(MISSING_REFRESH_TOKEN_MESSAGE);
        }
        cookieUtils.deleteCookie(httpServletResponse, appProperties.getJwt().getRefreshTokenCookieName());
        return 1;
    }
}
