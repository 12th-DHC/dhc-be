package project.dhc.domain.name;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class NameController {

    private final NameService nameService;

    @PatchMapping("/name")
    public ResponseEntity<String> registerName(
            @Valid @RequestBody NameRegisterRequest request,
            Authentication authentication
    ) {
        Integer roomNumber = Integer.parseInt(authentication.getName());

        nameService.registerName(roomNumber, request);

        return ResponseEntity.ok("이름 등록 완료");
    }
}
