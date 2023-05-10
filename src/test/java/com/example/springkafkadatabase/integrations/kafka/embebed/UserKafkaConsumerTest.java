package com.example.springkafkadatabase.integrations.kafka.embebed;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.kafka.UserKafkaConsumer;
import com.example.springkafkadatabase.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@EmbeddedKafka
@SpringBootTest( properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}" )
@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class UserKafkaConsumerTest
{
    private final String TOPIC_NAME = "com.example.kafka.user";

    private Producer<String, String> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private UserKafkaConsumer userKafkaConsumer;

    @MockBean
    private UserService userService;

    @Captor
    ArgumentCaptor<UserDTO> userArgumentCaptor;

    @Captor
    ArgumentCaptor<String> topicArgumentCaptor;

    @Captor
    ArgumentCaptor<Integer> partitionArgumentCaptor;

    @Captor
    ArgumentCaptor<Long> offsetArgumentCaptor;

    @DynamicPropertySource
    static void kafkaProperties( final DynamicPropertyRegistry registry )
    {
        registry.add( "spring.datasource.url", () -> "jdbc:h2:mem:test" );
        registry.add( "spring.datasource.driverClassName", () -> "org.h2.Driver" );
        registry.add( "spring.datasource.username", () -> "root" );
        registry.add( "spring.datasource.password", () -> "secret" );
        registry.add( "spring.flyway.enabled", () -> "false" );
    }

    @BeforeAll
    void setUp()
    {
        final Map<String, Object> configs = new HashMap<>( KafkaTestUtils.producerProps( embeddedKafkaBroker ) );
        producer = new DefaultKafkaProducerFactory<>( configs, new StringSerializer(),
                new StringSerializer() ).createProducer();
    }

    @Test
    void logKafkaMessages() throws JsonProcessingException
    {
        // Write a message (John Wick user) to Kafka using a test producer
        final UserDTO userDTOExpected = new UserDTO( "11111", "John", "Wick" );
        final String message = objectMapper.writeValueAsString( userDTOExpected );
        producer.send( new ProducerRecord<>( TOPIC_NAME, 0, userDTOExpected.getUuid(), message ) );
        producer.flush();

        // Read the message and assert its properties
        verify( userKafkaConsumer, timeout( 5000 ).times( 1 ) )
                .logKafkaMessages( userArgumentCaptor.capture(), topicArgumentCaptor.capture(),
                        partitionArgumentCaptor.capture(), offsetArgumentCaptor.capture() );

        final UserDTO userDTOActual = userArgumentCaptor.getValue();
        assertNotNull( userDTOActual );
        assertEquals( userDTOExpected.getUuid(), userDTOActual.getUuid() );
        assertEquals( userDTOExpected.getFirstName(), userDTOActual.getFirstName() );
        assertEquals( userDTOExpected.getLastName(), userDTOActual.getLastName() );
        assertEquals( TOPIC_NAME, topicArgumentCaptor.getValue() );
        assertEquals( 0, partitionArgumentCaptor.getValue() );
        assertEquals( 0, offsetArgumentCaptor.getValue() );

        verify( userService, times( 1 ) ).save( any( UserDTO.class ) );

    }
}