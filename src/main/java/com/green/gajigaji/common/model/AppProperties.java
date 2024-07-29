package com.green.gajigaji.common.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Jwt jwt = new Jwt();
    private final Oauth2 oauth2 = new Oauth2();

    @Getter
    @Setter
    public static class Jwt {


        private String secret;                  // secret
        private String headerSchemaName;        // header-schema-name
        private String tokenType;               // token-type
        private long accessTokenExpiry;         // access-token-expiry
        private long refreshTokenExpiry;        // refresh-token-expiry
        private int refreshTokenCookieMaxAge;
        private String refreshTokenCookieName;


        public void setRefreshTokenExpiry(long refreshTokenExpiry) {
            this.refreshTokenExpiry = refreshTokenExpiry;
            this.refreshTokenCookieMaxAge = (int)(refreshTokenExpiry * 0.001);
        }
    }
    @Getter
    @Setter
    public static class Oauth2 {
        private String authorizationRequestCookieName;
        private String redirectUriParamCookieName;
        private int cookieExpirySeconds;
        private List<String> authorizedRedirectUris;
    }
}
