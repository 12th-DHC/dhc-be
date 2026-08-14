package project.dhc.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.dhc.global.security.JWTAuthenticationFilter;
import project.dhc.global.util.JwtTokenProvider;

@Configuration // Spring에서 사용할 객체(Bean)를 만들어서 등록하는 곳
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // Spring Security 설정
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http.sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS // JWT를 사용하기 때문에 세션을 사용하지 않음
                        )
                ).csrf(csrf -> csrf.disable()) // CSRF는 JWT 방식의 REST API에서는 비활성화
                .authorizeHttpRequests(auth -> auth.requestMatchers("/users/login").permitAll() // 학생 로그인
                        .requestMatchers("/admin/login").permitAll() // 관리자 로그인
                        .anyRequest().authenticated() // 그 외 모든 API는 로그인 필요
                )

                .addFilterBefore(
                        new JWTAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}