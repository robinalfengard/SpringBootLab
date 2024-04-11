package com.example.springbootlabmessages.User;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public User findById(int id) {
        System.out.println(id + " sent to service");
        if (userRepository.findById(Long.valueOf(id)).get() != null) {
            return userRepository.findById((long) id).get();
        } else System.out.println("could not find user");
        return null;
    }

    public void save(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            System.out.println("" + user.getUsername() + " already exists");
        else
            userRepository.save(user);
    }

    public List<String> getAllUsers() {
        return userRepository.findAll().stream().map(User::getUsername).toList();
    }


    public void updateUser(User nyUser, User user) {
        nyUser.setName(user.getName());
        nyUser.setEmail(user.getEmail());
        nyUser.setProfilePicture(user.getProfilePicture());
        nyUser.setProfilePictureBytes(user.getProfilePictureBytes());
        nyUser.setUsername(user.getUsername());
        userRepository.save(nyUser);
    }

}
