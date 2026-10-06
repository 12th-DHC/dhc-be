package project.dhc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import project.dhc.domain.notification.NotificationPublisher;
import project.dhc.domain.notification.config.NotificationRabbitConfig;
import project.dhc.domain.notification.dto.NotificationMessage;
import tools.jackson.databind.json.JsonMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** Opt-in test against the local broker; uses and removes its own temporary queue. */
@EnabledIfEnvironmentVariable(named = "RUN_RABBITMQ_INTEGRATION", matches = "true")
class NotificationRabbitIntegrationTest {
    @Test
    void publishesJsonMessagesToRealRabbitMq() throws Exception {
        Properties settings = new Properties();
        try (var reader = Files.newBufferedReader(Path.of(".env"))) {
            settings.load(reader);
        }
        CachingConnectionFactory connection = new CachingConnectionFactory(
                settings.getProperty("RABBITMQ_HOST", "localhost"),
                Integer.parseInt(settings.getProperty("RABBITMQ_PORT", "5672")));
        connection.setUsername(settings.getProperty("RABBITMQ_USERNAME", "guest"));
        connection.setPassword(settings.getProperty("RABBITMQ_PASSWORD", "guest"));
        connection.setVirtualHost(settings.getProperty("RABBITMQ_VIRTUAL_HOST", "/"));
        if (Boolean.parseBoolean(settings.getProperty("RABBITMQ_SSL_ENABLED", "false"))) {
            connection.getRabbitConnectionFactory().useSslProtocol();
        }
        String queue = "notification.integration." + UUID.randomUUID();
        RabbitAdmin admin = new RabbitAdmin(connection);
        boolean declared = false;
        try {
            admin.declareQueue(new Queue(queue, false, true, false));
            declared = true;
            RabbitTemplate template = new RabbitTemplate(connection);
            template.setMessageConverter(new NotificationRabbitConfig().notificationMessageConverter());
            NotificationPublisher publisher = new NotificationPublisher(template, "", queue);
            var passed = new NotificationMessage("a@example.com", "학생A", 200, true, null);
            var failed = new NotificationMessage("b@example.com", "학생B", 200, false, "개인구역 청소");

            publisher.publishAll(List.of(passed, failed));

            for (var expected : List.of(passed, failed)) {
                var received = template.receive(queue, 5000);
                assertNotNull(received, "Message must reach the actual queue");
                assertEquals("application/json", received.getMessageProperties().getContentType());
                assertEquals(expected, JsonMapper.builder().build()
                        .readValue(received.getBody(), NotificationMessage.class));
            }
            assertNull(template.receive(queue), "Exactly two messages should have been published");
        } finally {
            try {
                if (declared) admin.deleteQueue(queue);
            } finally {
                connection.destroy();
            }
        }
    }
}
