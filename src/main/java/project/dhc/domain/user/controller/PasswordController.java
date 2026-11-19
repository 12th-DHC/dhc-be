package project.dhc.domain.user.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.dhc.domain.user.dto.request.PasswordChangeRequest;
import project.dhc.domain.user.dto.response.PasswordChangeResponse;
import project.dhc.domain.user.service.PasswordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PasswordController {

    private final PasswordService passwordService;

    // 토큰에서 인증된 사용자 정보를 사용
    @PatchMapping("/password")
    public PasswordChangeResponse changePassword(
            Authentication authentication,
            @Valid @RequestBody PasswordChangeRequest request
    ) {
        return passwordService.changePassword(authentication.getName(), request);
    }
}