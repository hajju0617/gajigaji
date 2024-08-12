package com.green.gajigaji.security.oauth2;

import com.green.gajigaji.common.model.AppProperties;
import com.green.gajigaji.common.model.CookieUtils;
import com.green.gajigaji.security.MyUser;
import com.green.gajigaji.security.MyUserDetails;
import com.green.gajigaji.security.MyUserOAuth2Vo;
import com.green.gajigaji.security.jwt.JwtTokenProviderV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;



@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(response.isCommitted()) {
            log.error("onAuthenticationSuccess - 응답이 만료됨");
            return;
        }
        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("targetUrl : {}", targetUrl);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String redirectUri = cookieUtils.getCookie(request
                                                , appProperties.getOauth2().getRedirectUriParamCookieName()
                                                , String.class);


        String targetUrl = redirectUri == null ? getDefaultTargetUrl() : redirectUri;

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();


        MyUserOAuth2Vo myUserOAuth2Vo = (MyUserOAuth2Vo) myUserDetails.getMyUser();


        MyUser myUser = MyUser.builder()
                                .userId(myUserOAuth2Vo.getUserId())
                                .role(myUserOAuth2Vo.getRole())
                                .build();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);


        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(response, appProperties.getJwt().getRefreshTokenCookieName());
        cookieUtils.setCookie(response
                            , appProperties.getJwt().getRefreshTokenCookieName()
                            , refreshToken
                            , refreshTokenMaxAge);


        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("user_id", myUserOAuth2Vo.getUserId())
                .queryParam("nm", myUserOAuth2Vo.getNm()).encode()
                .queryParam("pic", myUserOAuth2Vo.getPic())
                .queryParam("access_token", accessToken)
                .build()
                .toUriString();

    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        repository.removeAuthorizationRequestCookies(response);
    }


}
