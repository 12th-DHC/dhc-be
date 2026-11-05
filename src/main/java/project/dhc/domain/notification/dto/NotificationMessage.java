package project.dhc.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public record NotificationMessage(
        String email,
        String name,
        Integer room,
        boolean passed,
        String reason
) {
    public NotificationMessage {
        if (passed) {
            reason = null;
        }
    }
}
