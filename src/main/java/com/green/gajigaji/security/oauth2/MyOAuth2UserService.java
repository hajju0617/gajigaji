package com.green.gajigaji.security.oauth2;

import com.green.gajigaji.security.MyUserDetails;
import com.green.gajigaji.security.MyUserOAuth2Vo;
import com.green.gajigaji.security.SignInProviderType;
import com.green.gajigaji.security.oauth2.userinfo.OAuth2UserInfo;
import com.green.gajigaji.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.green.gajigaji.user.UserMapper;
import com.green.gajigaji.user.model.SignInReq;
import com.green.gajigaji.user.model.SignUpReq;
import com.green.gajigaji.user.model.SimpleInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 MyOAuth2UserService :
 OAuth2 제공자(구글, 카카오, 네이버 등)로부터 Access-Token 받은 후 loadUser 메소드가 호출이 된다. (스프링 시큐리티에 구현되어 있음)
 OAuth2 제공자로부터 사용자 정보를 가져온다. (이미 구현되어 있음. : super.loadUser(userRequest))
 OAuth2User 인터페이스를 구현한 객체(인증 객체)를 정리해서 리턴 한다. (MyOAuth2UserService 에서 해야될 작업)

  프론트에서 플랫폼 소셜 로그인 아이콘을 클릭하면(리다이렉트 정보 전달 : 로그인 완료 후 다시 돌아올 프론트 주소값)
  -> 백엔드에 요청이 간다 (사용자 : 어떤 소셜 로그인 하고 싶다 에 대한 정보가 전달) -> 백엔드는 리다이렉트 (OAuth2 제공자 로그인 화면)
  -> 해당 제공자의 아이디/비밀번호를 작성 후 로그인 처리
  -> 제공자는 인가 코드를 백엔드에게 보내준다.
  -> 백엔드는 인가 코드를 가지고 access-token을 발급 받는다.
  -> access-token 으로 사용자 정보(scope에 작성한 내용)를 받는다.
  -> 이후는 자체 로그인 처리
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final UserMapper mapper;
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        try {
            return this.process(userRequest);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }

    }
    private OAuth2User process(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);


        SignInProviderType signInProviderType = SignInProviderType.valueOf(userRequest.getClientRegistration()
                                                                                        .getRegistrationId()
                                                                                        .toUpperCase()
        );

        OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(signInProviderType, oAuth2User.getAttributes());


//        SignInReq signInParam = new SignInReq();
//        signInParam.setUserEmail(oAuth2UserInfo.getEmail());
//        signInParam.setProviderType(signInProviderType.name());
//        SimpleInfo simpleInfo = null;
//        String providerType = userRequest.getClientRegistration().getRegistrationId();
//        if (providerType.equals("kakao")) {
//            simpleInfo = mapper.getSimpleUserInfo(oAuth2UserInfo.getId());
//        } else {
//            simpleInfo = mapper.getSimpleUserInfo(oAuth2UserInfo.getEmail());
//        }
        SimpleInfo simpleInfo = mapper.getSimpleUserInfo(oAuth2UserInfo.getEmail());


//        SimpleInfo simpleInfo = mapper.getSimpleUserInfo(oAuth2UserInfo.getEmail());




        if(simpleInfo == null) {  // 회원가입 처리
            SignUpReq signUpParam = new SignUpReq();
            signUpParam.setProviderType(signInProviderType);
//            if (signInProviderType.toString().equals("KAKAO")) {
//                signUpParam.setUserEmail(oAuth2UserInfo.getId());
//            } else {
//                signUpParam.setUserEmail(oAuth2UserInfo.getEmail());
//            }
            signUpParam.setUserEmail(oAuth2UserInfo.getEmail());
            signUpParam.setUserNickname(oAuth2UserInfo.getName());
            signUpParam.setUserPic(oAuth2UserInfo.getProfilePicUrl());
            signUpParam.setUserRole("ROLE_USER");

            int result = mapper.postSignUp(signUpParam);

            simpleInfo = new SimpleInfo();
            simpleInfo.setUserSeq(signUpParam.getUserSeq());
            simpleInfo.setUserName(signUpParam.getUserName());
            simpleInfo.setUserPic(signUpParam.getUserPic());
            simpleInfo.setUserRole(signUpParam.getUserRole());

        } else {    // 이미 회원가입이 되어 있음
            if(simpleInfo.getUserPic() == null
                    || (simpleInfo.getUserPic().startsWith("http")
                    && !simpleInfo .getUserPic().equals(oAuth2UserInfo.getProfilePicUrl()))) {

            }
        }
//        List<String> roles = new ArrayList<>();
//        roles.add("ROLE_USER");     // 소셜 로그인은 그냥 하드코딩으로 처리 (소셜로그인으로 관리자와 같은 권한부여 x)


        MyUserOAuth2Vo myUserOAuth2Vo = new MyUserOAuth2Vo(simpleInfo.getUserSeq(), simpleInfo.getUserRole(), simpleInfo.getUserName(), simpleInfo.getUserPic());
        MyUserDetails signInUser = new MyUserDetails();
        signInUser.setMyUser(myUserOAuth2Vo);
        return signInUser;
    }
}
