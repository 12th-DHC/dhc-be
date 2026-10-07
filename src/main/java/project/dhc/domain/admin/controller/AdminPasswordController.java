package project.dhc.domain.admin.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // 비밀번호 변경
    @PatchMapping("/password")
    public AdminPasswordChangeResponse changePassword(
            @AuthenticationPrincipal String adminId, // 로그인한 어드민 Access Token
            @Valid @RequestBody AdminPasswordChangeRequest request // 현재 비밀번호와 새 비밀번호 / @Vaild : 유효성 검사 어노테이션
    ) {
        // 어드민 비밀번호 변경
        adminService.changePassword(
                Long.valueOf(adminId),
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        // 비밀번호 변경 응답 완료
        return new AdminPasswordChangeResponse(
                "비밀번호가 성공적으로 변경되었습니다."
        );
    }
}
