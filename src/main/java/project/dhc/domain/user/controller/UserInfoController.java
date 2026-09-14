package project.dhc.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.dhc.domain.user.dto.response.UserInfoResponse;
import project.dhc.domain.user.service.UserInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserInfoController {

    private final UserInfoService userInfoService;

    // 사용자 정보 조회
    @GetMapping("/info")
    public UserInfoResponse getUserInfo(
            @RequestHeader("Authorization")
            String token // 로그인한 사용자의 JWT를 Authorization 헤더로 받음
    ) {
        return userInfoService.getUserInfo(token); // 받은 요청을 service에 실제 처리를 맡김
    }
}
