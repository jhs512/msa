package com.ll.global.security;

import com.ll.global.rsData.RsData;
import com.ll.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
    private final CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    public SecurityFilterChain baseSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/h2-console/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts", "/api/*/posts/{postId:\\d+}/comments")
                                .permitAll()
                                .requestMatchers("/api/*/members/login", "/api/*/members/logout", "/api/*/members/join")
                                .permitAll()
                                .requestMatchers("/api/*/posts/statistics")
                                .hasRole("ADMIN")
                                .requestMatchers("/api/*/**")
                                .authenticated()
                                .anyRequest()
                                .permitAll()
                )
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement
                                        .sessionCreationPolicy(STATELESS)
                )
                .formLogin(
                        formLogin ->
                                formLogin.disable()
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf(
                        csrf ->
                                csrf.disable()
                )
                .oauth2Login(
                        oauth2Login ->
                                oauth2Login
                                        .successHandler(customAuthenticationSuccessHandler)
                                        .authorizationEndpoint(
                                                authorizationEndpoint ->
                                                        authorizationEndpoint
                                                                .authorizationRequestResolver(customAuthorizationRequestResolver)

                                        )
                )
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setContentType("application/json;charset=UTF-8");

                                            response.setStatus(401);
                                            response.getWriter().write(
                                                    Ut.json.toString(
                                                            new RsData("401-1", "사용자 인증정보가 올바르지 않습니다.")
                                                    )
                                            );
                                        }
                                )
                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) -> {
                                            response.setContentType("application/json;charset=UTF-8");

                                            response.setStatus(403);
                                            response.getWriter().write(
                                                    Ut.json.toString(
                                                            new RsData("403-1", "권한이 없습니다.")
                                                    )
                                            );
                                        }
                                )
                );

        return http.build();
    }
}
