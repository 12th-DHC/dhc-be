package project.dhc.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dhc.domain.user.dto.request.PasswordChangeRequest;
import project.dhc.domain.user.dto.response.PasswordChangeResponse;
import project.dhc.domain.user.service.PasswordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class PasswordController {

    private final PasswordService passwordService;

    @PatchMapping("/password")
    public PasswordChangeResponse changePassword(
            @RequestBody PasswordChangeRequest request
    ) {
        return passwordService.changePassword(request);
    }
}