package com.example.springbootlabmessages.Config;

public class GithubUser {
    private String login;
    private String userId;
    private String name;
    private String url;
    private String avatarUrl;

    private String email;

    public void setUserId(String userId) {
        this.userId =  userId;
    }

    public Long getUserId() {
        return Long.valueOf(this.userId);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
