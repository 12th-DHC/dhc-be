package project.dhc.cleaning;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CleaningCheckController {
    private final CleaningCheckService cleaningCheckService;

    @PostMapping("/admin/check/{roomNumber}")
    public ResponseEntity<Void> registerCleaningCheck (
            @PathVariable("roomNumber") Integer roomNumber,
            @RequestBody @Valid project.dhc.cleaning.dto.CleaningCheckRequest request
    ) {
        cleaningCheckService.registerCleaningCheck(roomNumber, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("admin/search/{roomNumber}")
    public ResponseEntity<CleaningSearch> adminCleaningSearch(
            @PathVariable(name = "roomNumber", required = true) int roomNumber,
            @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        CleaningSearch result = cleaningCheckService.getCleaningSearch(roomNumber, date);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/user/search")
    public ResponseEntity<CleaningSearch> CleaningSearch(
            Authentication authentication,
            @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        int roomNumber = Integer.parseInt(authentication.getName());
        CleaningSearch result = cleaningCheckService.getCleaningSearch(roomNumber, date);

        return ResponseEntity.ok(result);
    }
}
