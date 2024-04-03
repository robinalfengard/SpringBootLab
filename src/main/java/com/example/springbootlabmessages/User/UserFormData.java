package com.example.springbootlabmessages.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserFormData {
    private String username;
    private String name;
    private String email;
    private String url;
    private String login;
    private String profilePicture;
    UserService userService;

    public UserFormData(String username, String name, String email, String url, String login, String profilePicture) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.url = url;
        this.login = login;
        this.profilePicture = profilePicture;
    }

    public UserFormData() {
    }

    public User toEntity() {
        var user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setUrl(url);
        user.setLogin(login);
        user.setProfilePicture(profilePicture);
        return user;
    }
    public User updateUsertoEntity(int id) {
        var user =userService.findById(id);
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setUrl(url);
        user.setLogin(login);
        user.setProfilePicture(profilePicture);
        return user;
    }
}
