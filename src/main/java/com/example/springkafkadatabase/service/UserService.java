package com.example.springkafkadatabase.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.entity.User;

public interface UserService
{
    void save( UserDTO user);
    List<User> getUsers( String firstName);
    List<User> getAllUsers( Integer page, Integer pageSize);


}
