package project.dhc.domain.notification;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.dhc.domain.notification.dto.NotificationMessage;

import java.util.List;
import java.util.Objects;

@Component
public class NotificationPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public NotificationPublisher(RabbitTemplate rabbitTemplate,
                                 @Value("${notification.rabbitmq.exchange:}") String exchange,
                                 @Value("${notification.rabbitmq.routing-key:}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publish(NotificationMessage message) {
        validateRouting();
        validateMessage(message);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    // 학생별로 한 건씩 발행한다. 재호출하면 이미 발행된 메시지도 다시 발행된다.
    public void publishAll(List<NotificationMessage> messages) {
        validateRouting();
        List<NotificationMessage> snapshot = List.copyOf(messages);
        snapshot.forEach(this::validateMessage);
        for (NotificationMessage message : snapshot) {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        }
    }

    private void validateRouting() {
        // 빈 exchange는 RabbitMQ 기본 exchange를 의미하므로 허용한다.
        if (routingKey.isBlank()) {
            throw new IllegalStateException("NOTIFICATION_ROUTING_KEY 설정이 필요합니다.");
        }
    }

    private void validateMessage(NotificationMessage message) {
        Objects.requireNonNull(message, "알림 메시지가 필요합니다.");
        if (message.email() == null || message.email().isBlank()
                || message.name() == null || message.name().isBlank()
                || message.room() == null) {
            throw new IllegalArgumentException("알림 대상의 이메일, 이름, 호실이 필요합니다.");
        }
        if (!message.passed() && (message.reason() == null || message.reason().isBlank())) {
            throw new IllegalArgumentException("불합격 알림에는 사유가 필요합니다.");
        }
    }
}
