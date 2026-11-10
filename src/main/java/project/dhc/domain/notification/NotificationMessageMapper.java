package project.dhc.domain.notification;

import org.springframework.stereotype.Component;
import project.dhc.domain.notification.dto.NotificationMessage;
import project.dhc.global.exception.exceptions.NotificationDataIncompleteException;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class NotificationMessageMapper {
    public NotificationMessage create(String email, String name, Integer roomNumber,
                                      Boolean passed, String notpassReason, Boolean indPassed) {
        if (email == null || email.isBlank() || name == null || name.isBlank() || roomNumber == null || passed == null) {
            throw NotificationDataIncompleteException.EXCEPTION;
        }
        boolean indFailed = Boolean.FALSE.equals(indPassed);
        boolean finalPassed = passed && !indFailed;
        Set<String> reasons = new LinkedHashSet<>();
        if (!passed) {
            if (notpassReason == null || notpassReason.isBlank()) {
                throw NotificationDataIncompleteException.EXCEPTION;
            }
            for (String value : notpassReason.split("[/]")) {
                String reason = value.trim();
                if (!reason.isEmpty()) {
                    reasons.add(switch (reason) {
                        case "1" -> "침구정리";
                        case "2" -> "개인물품 및 의복정리";
                        case "3" -> "호실 내 소등하기";
                        case "4" -> "전기콘센트 뽑기";
                        case "5" -> "바닥정리 및 신발정리";
                        default -> reason;
                    });
                }
            }
            if (reasons.isEmpty()) {
                throw NotificationDataIncompleteException.EXCEPTION;
            }
        }
        if (indFailed) {
            reasons.add("개인구역 청소");
        }
        return new NotificationMessage(email, name, roomNumber, finalPassed, finalPassed ? null : String.join(", ", reasons));
    }
}
