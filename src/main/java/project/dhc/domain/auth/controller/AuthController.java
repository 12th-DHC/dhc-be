package project.dhc.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.dhc.domain.auth.dto.request.AdminLoginRequest;
import project.dhc.domain.auth.dto.request.RefreshTokenRequest;
import project.dhc.domain.auth.dto.request.UserLoginRequest;
import project.dhc.domain.auth.dto.response.LoginResponse;
import project.dhc.domain.auth.dto.response.LogoutResponse;
import project.dhc.domain.auth.service.AuthService;

@RestController
@RequestMapping("/auth") // 기본 주소
@RequiredArgsConstructor // 생성자 자동 생성
public class AuthController {

    private final AuthService authService;

    // 관리자 로그인
    @PostMapping("/admin/login")
    public LoginResponse adminLogin(
            @RequestBody AdminLoginRequest request
    ) {
        return authService.adminLogin(request);
    }

    // 사용자 로그인
    @PostMapping("/users/login")
    public LoginResponse userLogin(
            @RequestBody UserLoginRequest request
    ) {
        return authService.userLogin(request);
    }

    // Refresh Token을 이용한 Access Token 재발급
    @PostMapping("/refresh")
    public String refreshAccessToken(
            @RequestBody RefreshTokenRequest request
    ){
        return authService.refreshAccessToken(
                request.getRefreshToken()
        );
    }

    // 관리자 로그아웃
    @PostMapping("/admin/logout")
    public LogoutResponse adminLogout() {
        return authService.logout();
    }

    // 유저 로그아웃
    @PostMapping("/users/logout")
    public LogoutResponse userLogout() {
        return authService.logout();
    }
}