package com.example.springbootlabmessages.User;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(Object email) {
        return userRepository.findByEmail(email.toString());
    }

    public User findById(int id) {
        System.out.println(id + " sent to service");
        if(userRepository.findById(Long.valueOf(id)).get() != null){
            return userRepository.findById((long) id).get();
        }
        else System.out.println("could not find user");
        return null;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<String> getAllUsers() {
        return userRepository.findAll().stream().map(User::getUsername).toList();
    }
}
