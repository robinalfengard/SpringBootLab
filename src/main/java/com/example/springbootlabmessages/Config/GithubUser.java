package com.example.springbootlabmessages.Config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubUser {
    private String login;
    private String userId;
    private String name;
    private String url;
    private String avatarUrl;

    private String email;


}
