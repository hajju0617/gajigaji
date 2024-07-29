package com.green.gajigaji.security;


import com.green.gajigaji.security.jwt.JwtAuthenticationAccessDeniedHandler;
import com.green.gajigaji.security.jwt.JwtAuthenticationEntryPoint;
import com.green.gajigaji.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
JSP
요청시 저장 공간
PageContext : JSP 내부에서만 사용
Request : controller -> service -> JSP 전송 (Request는 일회용)
Session
Application
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;  // @Component로 빈등록을 하였기 때문에 DI가 된다.
//    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
//    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;
//    private final OAuth2AuthenticationSuccessHandler  oAuth2AuthenticationSuccessHandler;
//    private final MyOAuth2UserService myOAuth2UserService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        CommonOAuth2Provider a;


        return httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        )       .httpBasic(http -> http.disable())
                .formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(   // 로그인이 안 되어 있을때 접근 할 수 없는 곳
                                "/api/party/wish/**"
                                ,"/api/board/**"
                                ,"/api/join/**"
                                ,"/api/plan/**"
                                ,"/api/review/**"
                                ,"/api/member/**"
                                ,"/api/budget/**"
                        )
                                .authenticated()
                                .anyRequest().permitAll()


//                        auth.requestMatchers(
//                          "/api/user/sign-up"        // 회원 가입, 로그인 인증이 안 되어 있더라도
//                        , "/api/user/sign-in"                           // 사용 가능 하게 세팅
//                        , "/api/user/access-token"
//
//                        , "/swagger"            // swagger 사용할 수 있게 세팅
//                        , "/swagger-ui/**"      // ** 의미 : 뒤쪽에 어떤 값이 들어와도 상관없다는 의미
//                        , "/v3/api-docs/**"
//
//                        , "/pic/**"             // /pic/aaaa.jpg    /pic/aabb/abab.jpg   /pic/abcd/qwer/zxcv/ddsa.jpg
//                                                  // /pic/aaa.jpg   *가 하나면 이것만 가능
//                        , "/fimg/**"
//
//                        , "/"                   // localhost/8080 보이게 세팅
//                        , "/index.html"         // 프론트 화면이 보일 수 있게 세팅
//                        , "/css/**"             // css
//                        , "/js/**"              // 자바스크립트
//                        , "/static/**"
//
//                        // 프론트에서 사용하는 라우터 주소
//                        ,"/sign-in"
//                        ,"/sign-up"
//                        ,"/profile/*"
//                        ,"/feed"
//
//                        // actuator
//                        , "/actuator"
//                        , "/actuator/*"
//
//
//                        ).permitAll()
//                                .anyRequest().authenticated()   // 위쪽 주소를 제외하고는 로그인이 되어 있어야만 허용
//                                                                  =====> 적을 주소가 너무 많아서 반대로 적기로 결정

                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                                                         .accessDeniedHandler(new JwtAuthenticationAccessDeniedHandler())
                )

                .build();





    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



