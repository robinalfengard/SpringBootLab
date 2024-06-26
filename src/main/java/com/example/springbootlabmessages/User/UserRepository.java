package com.example.springbootlabmessages.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface UserRepository extends ListCrudRepository<User, Long> {

    User findByUsername(String username);


    boolean existsByUsername(String username);


    @Query(value = "SELECT * FROM user WHERE username LIKE :username%", nativeQuery = true)
    List<User> findAllByUsernameWildcard(String username);
}
