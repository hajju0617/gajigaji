package com.green.gajigaji.security.oauth2;
import com.green.gajigaji.common.model.AppProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationCheckRedirectUriFilter extends OncePerRequestFilter {
    private final AppProperties appProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();


        log.info("requestUri : {}", requestUri);

        if (requestUri.startsWith(appProperties.getOauth2().getBaseUri())) {



            String redirectUriParam = request.getParameter("redirect_uri");
            if (redirectUriParam != null && !(hasAuthorizedRedirectUri(redirectUriParam))) {

                String errRedirectUrl = UriComponentsBuilder.fromUriString("/err_message")
                        .queryParam("message", "유효한 Redirect URL이 아닙니다.").encode()
                        .toUriString();

                response.sendRedirect(errRedirectUrl);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizedRedirectUri (String uri) {
        URI clientRedirectUri = URI.create(uri);
        log.info("clientRedirectUri.getHost() : {}", clientRedirectUri.getHost());
        log.info("clientRedirectUri.getPort() : {}", clientRedirectUri.getPort());

        for (String redirectUri : appProperties.getOauth2().getAuthorizedRedirectUris()) {

            URI authorizedUri = URI.create(redirectUri);
            if (clientRedirectUri.getHost().equalsIgnoreCase(authorizedUri.getHost())

                    && clientRedirectUri.getPort() == authorizedUri.getPort()

                    && clientRedirectUri.getPath().equals(authorizedUri.getPath())
            ) {
                return true;
            }
        }
        return false;
    }
}



