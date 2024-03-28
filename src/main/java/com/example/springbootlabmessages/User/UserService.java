package com.example.springbootlabmessages.User;

import org.springframework.stereotype.Service;

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
}
