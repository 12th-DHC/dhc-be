package project.dhc.domain.notification.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record NotificationRequest(
        @NotNull(message = "검사 날짜가 필요합니다.") LocalDate date
) {}
