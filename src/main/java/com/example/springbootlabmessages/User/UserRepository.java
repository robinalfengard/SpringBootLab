package com.example.springbootlabmessages.User;

import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
