package com.example.springkafkadatabase.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.example.springkafkadatabase.entity.User;

public interface UserPagingAndSortingRepository extends PagingAndSortingRepository<User, Long>
{
}
