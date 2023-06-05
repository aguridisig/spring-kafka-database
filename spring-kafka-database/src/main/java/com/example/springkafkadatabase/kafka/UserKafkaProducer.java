package com.example.springkafkadatabase.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.example.springkafkadatabase.dto.UserDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserKafkaProducer
{
    private final KafkaTemplate<String, UserDTO> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topic;
    @Value("${spring.kafka.replication.factor:1}")
    private int replicationFactor;
    @Value("${spring.kafka.partition.number:1}")
    private int partitionNumber;

    public void writeToKafka(UserDTO user)
    {
        kafkaTemplate.send(topic, user.getUuid(), user);
    }
}
