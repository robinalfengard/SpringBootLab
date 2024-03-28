package com.example.springbootlabmessages.User;

import com.example.springbootlabmessages.Message.Message;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class User {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String url;
    private String login;
    private String profilePicture;

    public User() {}

    public User(Long id , String username, String firstName, String lastName, String email, String profilePicture) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    List<Message> messageList = new ArrayList<>();


}