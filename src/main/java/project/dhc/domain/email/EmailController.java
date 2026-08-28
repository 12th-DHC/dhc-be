package project.dhc.domain.email;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dhc.global.exception.exceptions.RoomNotFoundException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<String> registerEmail(
            @Valid @RequestBody EmailRegisterRequest request,
            HttpSession session
    ) {
        Integer roomNumber = (Integer) session.getAttribute("roomNumber");

        if (roomNumber == null) throw RoomNotFoundException.EXCEPTION;

        emailService.registerEmail(roomNumber, request);

        return ResponseEntity.ok("이메일 등록 완료");
    }
}
