package project.dhc.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dhc.domain.auth.dto.request.UserLoginRequest;
import project.dhc.domain.auth.dto.response.LoginResponse;
import project.dhc.domain.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserLoginRequest request){
        return userService.login(request);
    }
}
