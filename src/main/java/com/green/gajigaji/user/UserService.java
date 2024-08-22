package com.green.gajigaji.user;


import com.green.gajigaji.common.exception.CommonErrorCode;
import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.common.model.AppProperties;
import com.green.gajigaji.common.model.CookieUtils;
import com.green.gajigaji.common.model.CustomFileUtils;
import com.green.gajigaji.member.jpa.MemberRepository;
import com.green.gajigaji.security.AuthenticationFacade;
import com.green.gajigaji.security.MyUser;
import com.green.gajigaji.security.MyUserDetails;
import com.green.gajigaji.security.SignInProviderType;
import com.green.gajigaji.security.jwt.JwtTokenProviderV2;
import com.green.gajigaji.user.jpa.UserEntity;
import com.green.gajigaji.user.usercommon.CommonUser;
import com.green.gajigaji.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
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
    private final MemberRepository memberRepository;


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
        if(userRepository.existsUserEntityByUserEmail(p.getUserEmail())) {
            throw new CustomException(EMAIL_DUPLICATION_MESSAGE);
        }
        if(mapper.duplicatedCheckNumber(p.getUserPhone()) == 1) {
            throw new CustomException(NUMBER_DUPLICATION_MESSAGE);
        }
        if(userRepository.existsUserEntityByUserNickname(p.getUserNickname())) {
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

//    SimpleInfo userInfo = mapper.getSimpleUserInfo(p.getUserEmail());
    public SignInRes postSignIn(HttpServletResponse res, SignInReq p) {

        UserEntity userEntity = userRepository.findUserEntityByUserEmailAndProviderType(p.getUserEmail(), SignInProviderType.LOCAL);

        if(userEntity == null || !(p.getUserEmail().equals(userEntity.getUserEmail())) || !(passwordEncoder.matches(p.getUserPw(), userEntity.getUserPw()))) {
            throw new CustomException(INCORRECT_ID_PW);
        }

        MyUser myUser = MyUser.builder()
                .userId(userEntity.getUserSeq())
                .role(userEntity.getUserRole())
                .build();
        log.info("userInfo.getUserRole() : {}", userEntity.getUserRole());

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        log.info("appProperties.getJwt().getRefreshTokenCookieName() : {}", appProperties.getJwt().getRefreshTokenCookieName());
        cookieUtils.setCookie(res, appProperties.getJwt().getRefreshTokenCookieName(), refreshToken, refreshTokenMaxAge);

        return SignInRes.builder()                              // 프론트쪽 요청이 있어서 반환값 많음
                .userNickname(userEntity.getUserNickname())       // 소셜 로그인시 프론트에서 하드코딩 처리
                .userPic(userEntity.getUserPic())
                .userSeq(userEntity.getUserSeq())
                .userBirth(userEntity.getUserBirth())
                .userName(userEntity.getUserName())
                .userGender(userEntity.getUserGender())
                .userEmail(userEntity.getUserEmail())
                .userAddr(userEntity.getUserAddr())
                .userPhone(userEntity.getUserPhone())
                .userGender(userEntity.getUserGender())
                .accessToken(accessToken)
                .userRole(userEntity.getUserRole())
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

        UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);



        MyUser myUser = ((MyUserDetails)auth).getMyUser();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);

        Map map = new HashMap<>();
        map.put("accessToken", accessToken);
        return map;
    }

    public int patchPassword(UpdatePasswordReq p) {
        p.setUserSeq(authenticationFacade.getLoginUserId());
//        String userPw = mapper.getUserPw(p.getUserSeq());
        String userPw = userRepository.findUserPwByUserSeq(p.getUserSeq());

        if (!(passwordEncoder.matches(p.getUserPw(), userPw)) || !(p.getUserNewPw().equals(p.getUserPwCheck()))) {
            throw new CustomException(PASSWORD_CHECK_MESSAGE);
        }
        String newPassword = passwordEncoder.encode(p.getUserNewPw());
//        p.setUserNewPw(newPassword);

        UserEntity user = userRepository.findById(p.getUserSeq()).orElseThrow(() -> new CustomException(NOT_FOUND_MESSAGE));
        user.setUserPw(newPassword);
        userRepository.save(user);
        return 1;
    }

    @Transactional
    public int deleteUser() {
        long userPk = authenticationFacade.getLoginUserId();

        if (!userRepository.existsUserEntityByUserSeq(userPk)) {
            throw new CustomException(NOT_FOUND_MESSAGE);
        }
        if (mapper.existingPartiesCreatedByUser(userPk) != 0) {
            throw new CustomException(USER_PARTIES_EXIST_ERROR);
        }

        try {
            String midPath = String.format("user/%d", userPk);
            String delAbsoluteFolderPath = String.format("%s%s", customFileUtils.uploadPath, midPath);
            customFileUtils.deleteFolder(delAbsoluteFolderPath);
        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

        memberRepository.updatePartyMemberGb(userPk);
        mapper.deleteRejectedPartiesOnExit(userPk);
        return userRepository.deactivateUser(userPk);
    }

    public UserData getDetailUserInfo() {
        long userPk = authenticationFacade.getLoginUserId();
//        UserData userData = mapper.getDetailUserInfo(userPk);
        UserEntity userEntity = userRepository.findUserEntityByUserSeq(userPk);
        log.info("userEntity : {}", userEntity);

        return new UserData(userEntity);
    }

    public int duplicatedCheck(String str, int num) {   // 1 : 이메일, 2 : 닉네임
        boolean isAlreadyUsed = switch (num) {
            case 1 -> userRepository.existsUserEntityByUserEmail(str);
            case 2 -> userRepository.existsUserEntityByUserNickname(str);
            default -> throw new CustomException(INPUT_VALIDATION_MESSAGE);
        };
        if (isAlreadyUsed) {
            throw new CustomException(IS_DUPLICATE);
        }
        return 1;
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

    public String findUserId(FindIdReq p) {
//        String userEmail = mapper.findUserId(p);
        Date userBirth = CommonUser.convertToDate(p.getUserBirth());

        String userEmail = userRepository.findUserId(p.getUserName(), p.getUserPhone(), userBirth);
        if(userEmail == null) {
            throw new CustomException(NOT_FOUND_MESSAGE);
        }
        return CommonUser.maskEmail(userEmail);
    }

    public int logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie token = cookieUtils.getCookie(httpServletRequest, appProperties.getJwt().getRefreshTokenCookieName());   // request, refresh token
        if(token == null) {     // token => name : refresh token, value : refresh token 값
            throw new CustomException(MISSING_REFRESH_TOKEN_MESSAGE);
        }
        cookieUtils.deleteCookie(httpServletResponse, appProperties.getJwt().getRefreshTokenCookieName());  // refresh token
        return SUCCESS;
    }
}
