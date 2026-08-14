package project.dhc.cleaning;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dhc.cleaning.dto.CleaningCheckRequest;

@RestController
@RequestMapping("/admin/check")
@RequiredArgsConstructor
public class CleaningCheckController {
    private final CleaningCheckService cleaningCheckService;

    @PostMapping("/{roomId}")
    public ResponseEntity<Void> registerCleaningCheck (
            @PathVariable("roomId") Integer roomNumber,
            @RequestBody @Valid CleaningCheckRequest request
    ) {
        cleaningCheckService.registerCleaningCheck(roomNumber, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
