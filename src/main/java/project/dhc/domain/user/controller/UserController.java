package project.dhc.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dhc.domain.auth.dto.request.UserLoginRequest;
import project.dhc.domain.auth.dto.response.LoginResponse;
import project.dhc.domain.auth.service.AuthService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserLoginRequest request){
        return authService.userLogin(request);
    }
}
