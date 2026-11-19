package project.dhc.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.dhc.domain.user.dto.response.UserInfoResponse;
import project.dhc.domain.user.service.UserInfoService;
import project.dhc.global.response.ApiResponseMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserInfoController {

    private final UserInfoService userInfoService;

    // 사용자 정보 조회
    @GetMapping("/info")
    @ApiResponseMessage("정보 조회 완료")
    public UserInfoResponse getUserInfo(
            @AuthenticationPrincipal String roomNumber
    ) {
        return userInfoService.getUserInfo(roomNumber);
    }
}