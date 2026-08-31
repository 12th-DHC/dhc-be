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
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.ErrorCode;
import project.dhc.global.exception.exceptions.AdminNotFoundException;
import project.dhc.global.exception.exceptions.InvalidPasswordException;
import project.dhc.global.exception.exceptions.RoomNotFoundException;
import project.dhc.global.util.JwtTokenProvider;

@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class AuthService {

    private final AdminRepository adminRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 관리자 로그인
    public LoginResponse adminLogin(AdminLoginRequest request) {

        // 관리자 조회
        Admin admin = adminRepository.findById(1L).orElseThrow(() -> AdminNotFoundException.EXCEPTION);

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getAdminPassword(), admin.getAdminPassword())) {
            throw InvalidPasswordException.EXCEPTION;
        }

        // 관리자 JWT 생성
        String accessToken =
                jwtTokenProvider.createAccessToken(
                        "ADMIN",
                        "ADMIN"
                );

        return new LoginResponse(
                200,
                "어드민 로그인 완료",
                accessToken
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

        //사용자 JWT 생성
        String accessToken =
                jwtTokenProvider.createAccessToken(
                        String.valueOf(room.getRoomNumber()),
                        "USER"
                );
        return new LoginResponse(
                200,
                "로그인 완료",
                accessToken
        );
    }
    // 로그아웃 처리
    public LogoutResponse logout(){

        return new LogoutResponse(
                "로그아웃 완료"
        );
    }
}
