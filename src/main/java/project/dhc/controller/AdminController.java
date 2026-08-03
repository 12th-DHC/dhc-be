package project.dhc.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dhc.dto.request.AdminLoginRequest;
import project.dhc.dto.response.LoginResponse;
import project.dhc.service.AdminService;

@RestController // 스프링이 자동으로 JSON으로 응답
@RequestMapping("/admin") // 컨트롤러의 기본 주소
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login") // Post 요청을 받음
    public LoginResponse login(@RequestBody AdminLoginRequest request){
        return adminService.login(request); //로그인 처리는 adminService에서 처리하고 그 결과를 그대로 클라이언트에게 반환
    }
}
