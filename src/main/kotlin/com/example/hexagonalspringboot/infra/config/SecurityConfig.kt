package com.example.hexagonalspringboot.infra.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() } // JWT를 사용하기 때문에 세션을 사용하지 않음
//            .sessionManagement { configurer: SessionManagementConfigurer<HttpSecurity?> ->
//                configurer.sessionCreationPolicy(
//                    SessionCreationPolicy.STATELESS
//                )
//            }
            .authorizeHttpRequests { authorize ->
                authorize // 해당 API에 대해서는 모든 요청을 허가
                    .requestMatchers("/members/sign-up").permitAll() // ⭐️
                    .requestMatchers("/members/sign-in").permitAll()
                    .requestMatchers("/members/**").permitAll()
//                        .requestMatchers("/members/reissuanceAccessToken").permitAll()
//                        .requestMatchers("/goods/**")
//                        .permitAll() //                                .requestMatchers("/members/loginCheck").permitAll()
//                        .requestMatchers("/members/id/exists").permitAll() //테스트를 위해서 일단 모든 요청을 허가함 추후 삭제 필요
//                        .requestMatchers("/swagger-ui/**").permitAll()
//                        .requestMatchers("/orders/**").permitAll()
//                        .requestMatchers("/v3/api-docs/**").permitAll() // USER 권한이 있어야 요청할 수 있음
//                        .requestMatchers("/members/test").hasRole("USER") // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                    .anyRequest().authenticated()
            } // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}