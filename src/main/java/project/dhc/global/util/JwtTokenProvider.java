package project.dhc.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey; // JWT 서명에 사용할 비밀키
    private final long accessTokenExpiration; // Access Token의 유효시간
    private final long refreshTokenExpiration; // Refresh Token의 유효시간


    // application.yml의 값을 가져옴
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {

        // 문자열로 받은 비밀키를 JWT 서명용 SecretKey로 변환
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        // Access Token 유효시간 저장
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

     // Access Token 생성
     // param subject : 사용자 식별 정보
     // param role : 사용자 권한 (USER / ADMIN)
     // return 생성된 JWT
    public String createAccessToken(String subject, String role) {

        // 현재 시간
        Date now = new Date();

        // 만료 시간 계산
        Date expiration = new Date(
                now.getTime() + accessTokenExpiration
        );

        // JWT 생성
        return Jwts.builder()
                .subject(subject) // 사용자 식별 정보
                .claim("role", role) // 사용자 권한
                .claim("type", "ACCESS") // accessToken임을 표시
                .issuedAt(now) // JWT 발급 시간
                .expiration(expiration) // JWT 만료 시간
                .signWith(secretKey) // 비밀키를 이용해 서명
                .compact(); // JWT 문자열 생성
    }

    // Refresh Token 생성
    public String createRefreshToken(String subject) {

        // 현재 시간
        Date now = new Date();

        // 만료 시간 계산
        Date expiration = new Date(
                now.getTime() + refreshTokenExpiration
        );

        // JWT 생성
        return Jwts.builder()
                .subject(subject)
                .claim("type", "REFRESH") // refreshToken임을 표시
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey).compact();
    }


     // JWT가 정상적인 토큰인지 검사
     // return 정상적인 토큰이면 true
    public boolean validateToken(String token) {

        try {

            // JWT 서명 검증 + 만료시간 검증
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            // 토큰이 위조되었거나 만료된 경우
            return false;
        }
    }


    // JWT에서 Claims 가져오기
    // Claims = JWT 안에 들어있는 데이터
    public Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



    // JWT에서 사용자 식별 정보 가져오기
    public String getSubject(String token) {

        return getClaims(token)
                .getSubject();
    }



    // JWT에서 사용자 권한 가져오기
    public String getRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    // JWT의 토큰 종류 확인
    public String getTokenType(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // JWT 서명을 검증
                .build() //JWT 파서 생성
                .parseSignedClaims(token) // 토큰의 서명을 검증하고 claims를 파싱
                .getPayload() // 토큰에 담긴 payload를 가져옴
                .get("type", String.class); // payload에서 type 값을 String 타입으로 변환하고 가져옴
    }

    // Access Token인지 확인
    public boolean isAccessToken(String token) {
        return "ACCESS".equals(getTokenType(token));
    }
}