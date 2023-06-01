package com.example.emailnotification.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.example.emailnotification.dto.UserDTO;
import com.example.emailnotification.service.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserKafkaConsumer
{
    private final EmailService emailService;

    @KafkaListener(topics = "${spring.kafka.topic.name}",
            concurrency = "${spring.kafka.consumer.level.concurrency:3}")
    public void logKafkaMessages( final UserDTO user,
                                  @Header( KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                                  @Header(KafkaHeaders.OFFSET) Long offset ) throws MessagingException
    {
        log.info( "Received a message contains a user information with id {}, from {} topic, " +
                "{} partition, and {} offset", user.getUuid(), topic, partition, offset );
        emailService.sendMail(user);
    }
}
