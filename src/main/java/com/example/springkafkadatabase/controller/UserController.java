package com.example.springkafkadatabase.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.entity.User;
import com.example.springkafkadatabase.kafka.UserKafkaProducer;
import com.example.springkafkadatabase.service.UserService;
import com.github.javafaker.Faker;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag( name = "User", description = "User API" )
@RequestMapping("/v1/users")
@Slf4j
//@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    private final UserKafkaProducer kafkaProducer;
    private final Faker faker;

    public UserController(UserKafkaProducer kafkaProducer, UserService userService) {
        this.kafkaProducer = kafkaProducer;
        this.userService = userService;
        faker = new Faker();
    }

    @GetMapping( "/random" )
    @ResponseStatus( HttpStatus.OK )
    @Operation( summary = "Create a user",
            description = "Creates a random user and write it to Kafka which is consumed by the listener" )
    public void generateRandomUser()
    {
        kafkaProducer.writeToKafka(
                new UserDTO( UUID.randomUUID().toString(), faker.name().firstName(), faker.name().lastName() ) );
    }

    @GetMapping( "/random-sync" )
    @ResponseStatus( HttpStatus.OK )
    @Operation( summary = "Create a user",
            description = "Creates a random user and write it to Kafka which is consumed by the listener" )
    public void generateRandomUserBlock()
    {
        userService.save(
                new UserDTO( UUID.randomUUID().toString(), faker.name().firstName(), faker.name().lastName() ) );
    }

    @GetMapping( "/{firstName}" )
    @ResponseStatus( HttpStatus.OK )
    @Operation( summary = "Create a user",
            description = "Returns a list of users that matchers the given name" )
    public List<UserDTO> getUsers( @PathVariable( name = "firstName" ) String name )
    {
        List<User> users = userService.getUsers( name );

        return users.stream().map( user -> new UserDTO( user.getId(), user.getFirstName(), user.getLastName() ) )
                .collect( Collectors.toList() );
    }

    @GetMapping( "/all" )
    @ResponseStatus( HttpStatus.OK )
    @Operation( summary = "Find all users from database",
            description = "Returns a list of all users" )
    public Page<UserDTO> getAllUsers(  @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(defaultValue = "firstName") String sortBy)
    {
        final Page<User> users = userService.getAllUsers(page,pageSize,sortBy);
        log.info( "Total of users: "+users.getTotalElements()  );
        /*List<User> usersList = users.getContent()
        return usersList.stream().map( user -> new UserDTO( user.getId(), user.getFirstName(), user.getLastName() ) )
                .toList();*/
        return users.map( user -> new UserDTO( user.getId(), user.getFirstName(), user.getLastName() ) );
    }
}
