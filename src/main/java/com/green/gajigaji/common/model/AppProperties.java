package com.green.project2nd.common.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
<<<<<<< HEAD
@ConfigurationProperties(prefix = "app")
=======
@ConfigurationProperties(prefix = "app") // applicationl.yaml 파일(46번 라인)의 app 을 뜻함
//ConfigurationProperties : yaml에 작성되어 있는 데이터를 객체화 시켜주는 에노테이션
>>>>>>> c1deb1b (유저 - swagger 표시 작업, 생년월일 유효성 검사 작업, 이메일 인증번호 작업,  JWT 적용 작업 중,)
public class AppProperties {
    private final Jwt jwt = new Jwt();
    private final Oauth2 oauth2 = new Oauth2();

    @Getter
    @Setter
<<<<<<< HEAD
    public static class Jwt {
=======
    public static class Jwt { // Jwt = applicationl.yaml 파일(47번 라인)의 jwt 을 뜻함
        // 멤버필드명은 application.yaml 의 app/jwt/* 속성명과 매칭
        // application.yaml 에서 '-'는 멤버필드에서 카멜케이스 기법과 매칭

>>>>>>> c1deb1b (유저 - swagger 표시 작업, 생년월일 유효성 검사 작업, 이메일 인증번호 작업,  JWT 적용 작업 중,)


        private String secret;                  // secret
        private String headerSchemaName;        // header-schema-name
        private String tokenType;               // token-type
        private long accessTokenExpiry;         // access-token-expiry
        private long refreshTokenExpiry;        // refresh-token-expiry
        private int refreshTokenCookieMaxAge;
        private String refreshTokenCookieName;


        public void setRefreshTokenExpiry(long refreshTokenExpiry) {
            this.refreshTokenExpiry = refreshTokenExpiry;
<<<<<<< HEAD
            this.refreshTokenCookieMaxAge = (int)(refreshTokenExpiry * 0.001);
=======
            this.refreshTokenCookieMaxAge = (int)(refreshTokenExpiry * 0.001);      // yaml에서 시간 단위가 ms 라서 s로 변환 (ms = 1 / 1000초)
>>>>>>> c1deb1b (유저 - swagger 표시 작업, 생년월일 유효성 검사 작업, 이메일 인증번호 작업,  JWT 적용 작업 중,)
        }
    }
    @Getter
    @Setter
<<<<<<< HEAD
    public static class Oauth2 {
=======
    public static class Oauth2 {                        // yaml 파일에 oauth2 : 와 매칭 되는 클래스 (refresh-token-expiry 밑에 위치)
>>>>>>> c1deb1b (유저 - swagger 표시 작업, 생년월일 유효성 검사 작업, 이메일 인증번호 작업,  JWT 적용 작업 중,)
        private String authorizationRequestCookieName;
        private String redirectUriParamCookieName;
        private int cookieExpirySeconds;
        private List<String> authorizedRedirectUris;
    }
}
