package project.dhc.domain.admin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import project.dhc.domain.admin.dto.request.AdminPasswordChangeRequest;
import project.dhc.domain.admin.dto.response.AdminPasswordChangeResponse;
import project.dhc.domain.admin.service.AdminPasswordService;
import project.dhc.global.util.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPasswordController {

    private final AdminPasswordService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    // 비밀번호 변경
    @PatchMapping("/password")
    public AdminPasswordChangeResponse changePassword(
            @RequestHeader("Authorization") String authorization, // 로그인한 어드민 Access Token
            @RequestBody AdminPasswordChangeRequest request // 현재 비밀번호와 새 비밀번호
    ) {
        String token = authorization.substring(7); // "Bearer "를 제거하고 실제 JWT만 추출
        Long adminId = Long.valueOf(
                jwtTokenProvider.getSubject(token)
        );
        // 어드민 비밀번호 변경
        adminService.changePassword(
                adminId,
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        // 비밀번호 변경 응답 완료
        return new AdminPasswordChangeResponse(
                "비밀번호가 성공적으로 변경되었습니다."
        );
    }
}
