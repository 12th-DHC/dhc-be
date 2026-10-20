package project.dhc.domain.notification.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitConfig {
    @Bean
    public JacksonJsonMessageConverter notificationMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
