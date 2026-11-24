package project.dhc.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.admin.repository.AdminRepository;
import project.dhc.domain.auth.dto.request.AdminLoginRequest;
import project.dhc.domain.auth.dto.request.UserLoginRequest;
import project.dhc.domain.auth.dto.response.LoginResponse;
import project.dhc.domain.auth.dto.response.LogoutResponse;
import project.dhc.domain.auth.dto.response.RefreshTokenResponse;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.*;
import project.dhc.global.util.JwtTokenProvider;

import java.time.Duration;

@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class AuthService {

    private final AdminRepository adminRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;


    // 관리자 로그인
    public LoginResponse adminLogin(AdminLoginRequest request) {

        // 관리자 조회
        Admin admin = adminRepository.findByAdminUsername(request.getAdminUsername()).orElseThrow(() -> AdminNotFoundException.EXCEPTION);

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getAdminPassword(), admin.getAdminPassword())) {
            throw InvalidPasswordException.EXCEPTION;
        }

        // 관리자 ID를 토큰의 사용자 식별 정보로 사용
        String subject = String.valueOf(admin.getAdminId());

        // AccessToken, refreshToken 발급
        String accessToken =
                jwtTokenProvider.createAccessToken(
                        String.valueOf(admin.getAdminId()),
                        "ADMIN"
                );

        String refreshToken = jwtTokenProvider.createRefreshToken(subject, "ADMIN");

        // Redis에 Refresh Token 저장
        redisTemplate.opsForValue().set(
                getRefreshTokenKey("ADMIN", subject),
                refreshToken,
                Duration.ofMillis(refreshTokenExpiration)
        );

        // 로그인 응답
        return new LoginResponse(
                200,
                "어드민 로그인 완료",
                accessToken,
                refreshToken
        );
    }

    // 사용자 로그인
    public LoginResponse userLogin(UserLoginRequest request) {
        
        // 방 번호로 조회
        Room room = roomRepository.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> RoomNotFoundException.EXCEPTION);

        // 비밀번호 확인
        if(!passwordEncoder.matches(request.getRoomPassword(), room.getRoomPassword())) {
            throw InvalidPasswordException.EXCEPTION;
        }

        // Access Token 생성
        String accessToken =
                jwtTokenProvider.createAccessToken(
                        String.valueOf(room.getRoomNumber()),
                        "USER"
                );

        // Refresh Token 생성
        String subject = String.valueOf(room.getRoomNumber());
        String refreshToken = jwtTokenProvider.createRefreshToken(subject, "USER");

        // Redis에 Refresh Token 저장
        redisTemplate.opsForValue().set(
                getRefreshTokenKey("USER", subject),
                refreshToken,
                Duration.ofMillis(refreshTokenExpiration)
        );

        // 로그인 응답
        return new LoginResponse(
                200,
                "로그인 완료",
                accessToken,
                refreshToken
        );
    }

    // Refresh Token 검증 및 Access Token 재발급
    public RefreshTokenResponse refreshAccessToken(String refreshToken) {

        // refreshToken JWT 검증
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        // Refresh Token 종류 확인
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        String subject = jwtTokenProvider.getSubject(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        // 관리자 또는 사용자 토큰인지 확인
        if (!"ADMIN".equals(role) && !"USER".equals(role)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        String savedToken = redisTemplate.opsForValue().get(getRefreshTokenKey(role, subject));

        // Redis에 저장된 토큰인지 확인
        if (!refreshToken.equals(savedToken)) {
            throw RefreshTokenNotFoundException.EXCEPTION;
        }

        return new RefreshTokenResponse(jwtTokenProvider.createAccessToken(subject, role));
    }
    // 어드민 로그아웃 처리
    public LogoutResponse adminLogout(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> AdminNotFoundException.EXCEPTION);

        redisTemplate.delete(getRefreshTokenKey("ADMIN", String.valueOf(adminId)));
        return new LogoutResponse("로그아웃 완료");
    }

    public LogoutResponse userLogout(Integer roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> RoomNotFoundException.EXCEPTION);

        redisTemplate.delete(getRefreshTokenKey("USER", String.valueOf(roomNumber)));
        return new LogoutResponse("로그아웃 완료");
    }

    // Redis에서 사용할 Refresh Token 키 생성
    private String getRefreshTokenKey(String role, String subject) {
        return "refreshToken:" + role + ":" + subject;
    }
}
