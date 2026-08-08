package project.dhc.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dhc.dto.request.UserLoginRequest;
import project.dhc.dto.response.LoginResponse;
import project.dhc.global.exception.exceptions.RoomNotFoundException;
import project.dhc.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import project.dhc.dto.request.EmailRegisterRequest;
import project.dhc.global.exception.BusinessException;
import project.dhc.global.exception.ErrorCode;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody UserLoginRequest request,
            HttpSession session
    ) {
        LoginResponse response = userService.login(request);
        session.setAttribute("roomNumber", request.getRoomNumber());

        return response;
    }
    @PostMapping("/email")
    public ResponseEntity<String> registerEmail(
            @Valid @RequestBody EmailRegisterRequest request,
            HttpSession session
    ) {
        Integer roomNumber = (Integer) session.getAttribute("roomNumber");

        if (roomNumber == null) {
            throw RoomNotFoundException.EXCEPTION;
        }

        userService.registerEmail(roomNumber, request);

        return ResponseEntity.ok("이메일 등록 완료");
    }
}
