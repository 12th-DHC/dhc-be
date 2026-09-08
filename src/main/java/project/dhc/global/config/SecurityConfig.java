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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration // Spring에서 사용할 객체(Bean)를 만들어서 등록하는 곳
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // Spring Security 설정
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
            ) throws Exception {

        http.cors(cors ->
                cors.configurationSource(corsConfigurationSource())
        )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS // JWT를 사용하기 때문에 세션을 사용하지 않음
                        )
                ).csrf(csrf -> csrf.disable()) // CSRF는 JWT 방식의 REST API에서는 비활성화(CSRF 보호 기능을 꺼서 인증없이 접근 허용)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/users/login").permitAll() // 학생 로그인
                        .requestMatchers("/auth/admin/login").permitAll() // 관리자 로그인
                        .anyRequest().authenticated() // 그 외 모든 API는 로그인 필요
                )
        // JWT 인증 필터를 Spring Security 필터 순서에 등록
                .addFilterBefore(
                        new JWTAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //허용할 프론트엔드 주소
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "https://dhc-fe-dev-user-pwa.sungju.xyz",
                "https://dhc-fe-dev-admin-pwa.sungju.xyz",
                "https://dhc-fe-dev-admin-web.sungju.xyz"
        ));
        // 허용할 HTTP Methods
        corsConfiguration.setAllowedMethods(List.of(
                "POST",
                "GET",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));
        corsConfiguration.setAllowedHeaders(List.of("*")); // 모든 헤더 요청 허용
        corsConfiguration.setAllowCredentials(true); // 인증 정보가 포함된 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 API에 CORS 적용

        return source;
    }
}