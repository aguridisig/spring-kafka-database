package com.example.springkafkadatabase.service;

import java.util.List;
import com.example.springkafkadatabase.dto.UserDTO;
import com.example.springkafkadatabase.entity.User;

public interface UserService
{
    void save( UserDTO user);
    List<User> getUsers( String firstName);
}
