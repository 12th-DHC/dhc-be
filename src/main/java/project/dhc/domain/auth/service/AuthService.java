package project.dhc.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.admin.repository.AdminRepository;
import project.dhc.domain.auth.dto.request.AdminLoginRequest;
import project.dhc.domain.auth.dto.request.UserLoginRequest;
import project.dhc.domain.auth.dto.response.LoginResponse;
import project.dhc.domain.auth.dto.response.LogoutResponse;
import project.dhc.domain.auth.entity.RefreshToken;
import project.dhc.domain.auth.repository.RefreshTokenRepository;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.*;
import project.dhc.global.util.JwtTokenProvider;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class AuthService {

    private final AdminRepository adminRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
                        subject,
                        "ADMIN"
                );

        String refreshToken =
                jwtTokenProvider.createRefreshToken(subject);

        //refreshToken DB 저장
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setAdmin(admin);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiration(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(refreshTokenEntity);

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
        String refreshToken =
                jwtTokenProvider.createRefreshToken(
                        String.valueOf(room.getRoomNumber())
                );

        // Refresh Token DB 저장
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setRoom(room);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiration(
                LocalDateTime.now().plusDays(7)
        );

        refreshTokenRepository.save(refreshTokenEntity);

        // 로그인 응답
        return new LoginResponse(
                200,
                "로그인 완료",
                accessToken,
                refreshToken
        );
    }

    // Refresh Token 검증 및 Access Token 재발급
    public String refreshAccessToken(String refreshToken) {

        // refreshToken JWT 검증
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }
        
        // DB에서 Refresh Token 조회
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> RefreshTokenNotFoundException.EXCEPTION);

        // 어드민 Refresh Token인 경우
        if(token.getAdmin() != null) {
            String subject = String.valueOf(
                    token.getAdmin().getAdminId()
            );

            return jwtTokenProvider.createAccessToken(
                    subject,
                    "ADMIN"
            );
        }
        // 유저 Refresh Token인 경우
        if(token.getRoom() != null) {
            String subject = String.valueOf(
                    token.getRoom().getRoomNumber()
            );

            return jwtTokenProvider.createAccessToken(
                    subject,
                    "USER"
            );
        }
        // 어드민 또는 유저가 연결되지 않은 경우
        throw InvalidRefreshTokenException.EXCEPTION;
    }
    // 로그아웃 처리
    public LogoutResponse logout(){

        return new LogoutResponse(
                "로그아웃 완료"
        );
    }
}
