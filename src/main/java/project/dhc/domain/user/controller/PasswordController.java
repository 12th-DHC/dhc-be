package project.dhc.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.dhc.domain.user.dto.request.PasswordChangeRequest;
import project.dhc.domain.user.dto.response.PasswordChangeResponse;
import project.dhc.domain.user.service.PasswordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class PasswordController {

    private final PasswordService passwordService;

    @PatchMapping("/password")
    public PasswordChangeResponse changePassword(
            @RequestBody PasswordChangeRequest request
    ) {
        return passwordService.changePassword(request);
    }
}