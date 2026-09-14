package project.dhc.domain.reset;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dhc.domain.reset.dto.AdminResetRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ResetController {
    private final ResetService resetService;

    @PostMapping("/admin/reset")
    public void reset(@Valid @RequestBody AdminResetRequest request) {
        resetService.reset(request);
    }
}
