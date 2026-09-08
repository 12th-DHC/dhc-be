package project.dhc.global.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import project.dhc.global.util.JwtTokenProvider;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter : Spring에서 제공하는 필터 클래스, HTTP 요청 하나당 이 필터를 한 번 실행

    private final JwtTokenProvider jwtTokenProvider;

    // 모든 http 요청이 들어올 때 실행되는 필터
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, // 들어온 http 요청
            HttpServletResponse response, // 서버가 클라이언트에게 보내는 응답
            FilterChain filterChain // 다음 필터로 요청을 넘겨주는 역할
    )
        throws ServletException, IOException {

        String authorization =
                request.getHeader("Authorization");

        // authorization 헤더가 없으면 jwt 인증을 시도하지 않고 바로 다음 필터로 넘어감
        if(authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }
        //Bearer 로 시작하는지 확인
        if(!authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //"Bearer " 뒤에 있는 실제 JWT만 추출
        String token = authorization.substring(7);

        // JWT가 정상적인 토큰인지 확인
        if(!jwtTokenProvider.validateToken(token)) {
            // 잘못된 토큰이면 다음 필터로 넘김, 실제 보호된 API에서는 인증되지 않은 요청으로 처리
            filterChain.doFilter(request, response);
            return;
        }
        // JWT에서 사용자 식별 정보 가져오기
        String subject =
                jwtTokenProvider.getSubject(token);

        // 권한 가져오기
        String role =
                jwtTokenProvider.getRole(token);

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + role);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        List.of(authority)
                        );

        // 현재 요청을 인증된 사용자로 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}