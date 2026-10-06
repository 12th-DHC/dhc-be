package project.dhc.domain.notification;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dhc.domain.notification.dto.NotificationRequest;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/admin/alarm")
    public ResponseEntity<Void> publish(@Valid @RequestBody NotificationRequest request) {
        notificationService.publish(request.date());
        return ResponseEntity.accepted().build();
    }
}
