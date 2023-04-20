package com.example.springkafkadatabase.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.entity.User;
import com.example.springkafkadatabase.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{

    private final UserRepository userRepository;

    @Override
    public void save( final UserDTO userDto )
    {
        userRepository.save( User.builder()
                .id( userDto.getUuid() )
                .firstName( userDto.getFirstName() )
                .lastName( userDto.getLastName() )
                .build() );
    }

    @Override
    public List<User> getUsers( final String firstName )
    {
        return userRepository.getByFirstNameIgnoreCaseOrderByFirstNameAscLastNameAsc( firstName );
    }
}
