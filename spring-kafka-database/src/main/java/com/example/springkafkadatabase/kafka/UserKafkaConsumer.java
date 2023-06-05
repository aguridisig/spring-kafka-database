package com.example.springkafkadatabase.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.service.UserService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserKafkaConsumer
{
    private final UserService userService;

    @KafkaListener(topics = "${spring.kafka.topic.name}",
            concurrency = "${spring.kafka.consumer.level.concurrency:3}")
    public void logKafkaMessages( final UserDTO user,
                                  @Header( KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                                  @Header(KafkaHeaders.OFFSET) Long offset )
    {
        log.info( "Received a message contains a user information with id {}, from {} topic, " +
                "{} partition, and {} offset. Database Consumer", user.getUuid(), topic, partition, offset );
        userService.save( user );
    }
}
