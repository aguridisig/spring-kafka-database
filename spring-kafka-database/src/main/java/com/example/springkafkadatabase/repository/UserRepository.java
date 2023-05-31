package com.example.springkafkadatabase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springkafkadatabase.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{

    List<User> getByFirstNameIgnoreCaseOrderByFirstNameAscLastNameAsc( String firstName);

}