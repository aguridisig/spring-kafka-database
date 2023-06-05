package com.example.springkafkadatabase.integrations.kafka.testcontainers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.entity.User;
import com.example.springkafkadatabase.service.UserService;

@Testcontainers
@SpringBootTest
class MySqlIntegrationTest
{

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>( DockerImageName.parse("mysql:8.0-debian"));

    @DynamicPropertySource
    static void dynamicProperties( DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
        registry.add("spring.flyway.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.flyway.user", () -> mySQLContainer.getUsername());
        registry.add("spring.flyway.password", () -> mySQLContainer.getPassword());

    }

    @Autowired
    private UserService userService;

    @Test
    @Order( -1 )
    void testSaveUser(){
        userService.save(new UserDTO( UUID.randomUUID().toString(), "John", "McClane"));
        userService.save(new UserDTO(UUID.randomUUID().toString(), "Chandler", "Bing"));
        userService.save(new UserDTO(UUID.randomUUID().toString(), "Joey", "Tribbiani"));
        userService.save(new UserDTO(UUID.randomUUID().toString(), "John", "Kennedy"));

        final List<User> users = userService.getUsers("John");

        assertNotNull(users);
        assertEquals(4, users.size());
        assertEquals("Kennedy", users.get(0).getLastName());
        assertEquals("McClane", users.get(1).getLastName());
        assertEquals("Rambo", users.get(2).getLastName());
        assertEquals("Wick", users.get(3).getLastName());
    }

    @Test
    void testSaveUserThrowsExceptionOnDuplicateFirstNameAndLastName() {
        assertThrows( DataIntegrityViolationException.class,
                () -> userService.save(new UserDTO(UUID.randomUUID().toString(), "John", "Wick")),
                "Duplicate entry 'John-Wick' for key 'users.uc_user_first_last_name");
    }

    @Test
    @Order( 1 )
    void testGetAllUsers(){
        final Page<User> usersList = userService.getAllUsers(0, 10, "firstName");
        assertNotNull(usersList);
        assertEquals(9, usersList.getContent().size());
    }

}
