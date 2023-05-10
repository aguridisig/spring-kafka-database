package com.example.springkafkadatabase.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.entity.User;
import com.example.springkafkadatabase.kafka.UserKafkaProducer;
import com.example.springkafkadatabase.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

@ExtendWith( MockitoExtension.class )
class UserControllerTest
{
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private UserKafkaProducer userKafkaProducer;

    @BeforeEach
    void setUp()
    {
        mockMvc = MockMvcBuilders.standaloneSetup( userController ).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void generateRandomUser() throws Exception
    {
        doNothing().when( userKafkaProducer ).writeToKafka( any( UserDTO.class ) );

        final MockHttpServletResponse response = mockMvc.perform( get( "/v1/users/random" ) ).andReturn()
                .getResponse();

        assertEquals( HttpStatus.OK.value(), response.getStatus() );

        verify( userKafkaProducer, times( 1 ) ).writeToKafka( any( UserDTO.class ) );
    }

    @Test
    void generateRandomUserBlock() throws Exception
    {

        doNothing().when( userService ).save( any( UserDTO.class ) );

        final MockHttpServletResponse response = mockMvc.perform( get( "/v1/users/random-sync" ) ).andReturn()
                .getResponse();

        assertEquals( HttpStatus.OK.value(), response.getStatus() );
        verify( userService, times( 1 ) ).save( any( UserDTO.class ) );
    }

    @Test
    void getUsers() throws Exception
    {
        final List<User> expectedUserList =
                List.of( User.builder().id( "1" ).build(), User.builder().id( "2" ).build() );


        doReturn( expectedUserList ).when( userService ).getUsers( anyString() );

        final MockHttpServletResponse response = mockMvc.perform( get( "/v1/users/{name}","John" ) ).andReturn()
                .getResponse();

        final List<UserDTO> actualUserList = objectMapper.readValue( response.getContentAsString(),
                new TypeReference<List<UserDTO>>(){} );


        assertEquals( HttpStatus.OK.value(), response.getStatus() );
        verify( userService, times( 1 ) ).getUsers( anyString() );
        assertEquals( expectedUserList.size(), actualUserList.size() );
        assertEquals( expectedUserList.get( 0 ).getId(), actualUserList.get( 0 ).getUuid() );
        assertEquals( expectedUserList.get( 1 ).getId(), actualUserList.get( 1 ).getUuid() );


    }

    @Test
    void getAllUsers() throws Exception
    {
        final List<User> expectedUserList =
                List.of( User.builder().id( "1" ).build(), User.builder().id( "2" ).build() );

        final Page<User> page = new PageImpl<>(expectedUserList);

        doReturn( page ).when( userService ).getAllUsers( anyInt(),anyInt(),anyString() );

        final MockHttpServletResponse response = mockMvc.perform( get( "/v1/users/all" )
                        .param( "page",String.valueOf( 1 ) )
                        .param( "size",String.valueOf( 10 ) )
                        .param( "sort","id" ))
                .andReturn().getResponse();

        final List<UserDTO> actualUserList = objectMapper.readValue( response.getContentAsString(),
                new TypeReference<List<UserDTO>>(){} );

        verify( userService, times( 1 ) ).getAllUsers( anyInt(),anyInt(),anyString() );
        assertEquals( expectedUserList.size(), actualUserList.size() );
        assertEquals( expectedUserList.get( 0 ).getId(), actualUserList.get( 0 ).getUuid() );
        assertEquals( expectedUserList.get( 1 ).getId(), actualUserList.get( 1 ).getUuid() );
    }
}