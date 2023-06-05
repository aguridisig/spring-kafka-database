package com.example.springkafkadatabase.integrations.kafka.testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.kafka.UserKafkaProducer;
import com.example.springkafkadatabase.service.UserService;

@Testcontainers
@SpringBootTest
class KafkaIntegrationTest
{

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:test");
        registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "root");
        registry.add("spring.datasource.password", () -> "secret");
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @Autowired
    private UserKafkaProducer userKafkaProducer;

    @MockBean
    private UserService userService;

    @Test
    void testProduceAndConsumeKafkaMessage() {
        final ArgumentCaptor<UserDTO> captor = ArgumentCaptor.forClass(UserDTO.class);
        final UserDTO user = new UserDTO("11111", "John", "Wick");

        userKafkaProducer.writeToKafka(user);

        verify(userService, timeout(5000)).save(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals("11111", captor.getValue().getUuid());
        assertEquals("John", captor.getValue().getFirstName());
        assertEquals("Wick", captor.getValue().getLastName());
    }
}
