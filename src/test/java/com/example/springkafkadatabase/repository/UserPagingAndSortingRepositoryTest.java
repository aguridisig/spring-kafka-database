package com.example.springkafkadatabase.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import com.example.springkafkadatabase.entity.User;
import com.example.springkafkadatabase.repository.UserPagingAndSortingRepository;

@DataJpaTest
@DirtiesContext
class UserPagingAndSortingRepositoryTest
{
    @Autowired
    private UserPagingAndSortingRepository userPagingAndSortingRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testGetAllUsers(){

        testEntityManager.persist(stubUser("john", "Wick"));
        testEntityManager.persist(stubUser("Robert", "McCall"));
        testEntityManager.persist(stubUser("John", "Rambo"));

        final Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName").ascending());
        final Page<User> users = userPagingAndSortingRepository.findAll(pageable);
        assertNotNull(users);
        assertEquals(3, users.getContent().size());
    }

    private User stubUser( String firstName, String lastName) {
        return new User( UUID.randomUUID().toString(), firstName, lastName);
    }

}