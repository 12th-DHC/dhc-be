package project.dhc.domain.email;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<String> registerEmail(
            @Valid @RequestBody EmailRegisterRequest request,
            Authentication authentication
    ) {
        Integer roomNumber = Integer.parseInt(authentication.getName());

        emailService.registerEmail(roomNumber, request);

        return ResponseEntity.ok("이메일 등록 완료");
    }
}
