package com.green.gajigaji.security;


import com.green.gajigaji.common.model.AppProperties;
import com.green.gajigaji.security.jwt.JwtAuthenticationAccessDeniedHandler;
import com.green.gajigaji.security.jwt.JwtAuthenticationEntryPoint;
import com.green.gajigaji.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;  // @Component로 빈등록을 하였기 때문에 DI가 된다.
    private final AppProperties appProperties;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationAccessDeniedHandler jwtAuthenticationAccessDeniedHandler;
//    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
//    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;
//    private final OAuth2AuthenticationSuccessHandler  oAuth2AuthenticationSuccessHandler;
//    private final MyOAuth2UserService myOAuth2UserService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        CommonOAuth2Provider a;


        return httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        )       .httpBasic(http -> http.disable()).formLogin(form -> form.disable()).csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                "/api/user/sign_up"
                                ,"/api/user/sign_in").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/party").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/party/detail").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/party/location").permitAll()
                                .requestMatchers(HttpMethod.GET,  "/api/user/access-token","/api/review").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/user/findid").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/findpw").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/review/party").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/plan/party").permitAll()

                                .requestMatchers(
                                "/api/party/**"
                                ,"/api/party"
                                ,"/api/board/**"
                                ,"/api/board"
                                ,"/api/join"
                                ,"/api/join/**"
                                ,"/api/plan/**"
                                ,"/api/plan"
                                ,"/api/review/**"
                                ,"/api/review"
                                ,"/api/member/**"
                                ,"/api/member"
                                ,"/api/budget/**"
                                ,"/api/budget"
                                ,"/api/user/**"
                                ,"/api/user"
                                ,"/mailSend"
                                ,"/mailauthCheck"
                                )
                                .authenticated()   // 로그인이 안 되어 있을때 접근 할 수 없는 곳
                                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint)
                                                         .accessDeniedHandler(jwtAuthenticationAccessDeniedHandler)

                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



