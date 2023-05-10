package com.example.springkafkadatabase.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.entity.User;
import com.example.springkafkadatabase.repository.UserPagingAndSortingRepository;
import com.example.springkafkadatabase.repository.UserRepository;
import com.example.springkafkadatabase.service.UserServiceImpl;

@ExtendWith( MockitoExtension.class )
class UserServiceImplTest
{
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserPagingAndSortingRepository userPagingAndSortingRepositoryMock;

    @Test
    void save()
    {

        when(userRepositoryMock.save(any(User.class))).thenReturn( User.builder().build());

        userServiceImpl.save(UserDTO.builder().build());

        verify(userRepositoryMock, times(1)).save(any(User.class));
    }

    @Test
    void getUsers()
    {

        when(userRepositoryMock.getByFirstNameIgnoreCaseOrderByFirstNameAscLastNameAsc(anyString()))
                .thenReturn( java.util.List.of(User.builder().id(UUID.randomUUID().toString()).build()));

        userServiceImpl.getUsers("John");

        verify(userRepositoryMock, times(1)).getByFirstNameIgnoreCaseOrderByFirstNameAscLastNameAsc(anyString());
    }

    @Test
    void getAllUsers()
    {
        final User user = User.builder().id(UUID.randomUUID().toString()).build();

        when( userPagingAndSortingRepositoryMock.findAll(any(Pageable.class)))
                .thenReturn( new PageImpl<>(java.util.List.of(user)));

        userServiceImpl.getAllUsers(0, 10, "firstName");

        verify(userPagingAndSortingRepositoryMock, times(1)).findAll(any( Pageable.class));
    }
}