package com.example.springbootlabmessages.Config;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GithubService {
    private final RestClient restClient;

    public GithubService(RestClient restClient) {
        this.restClient = restClient;
    }


    public List<Email> getEmails(OAuth2AccessToken accessToken) {
        return restClient.get()
                .uri("https://api.github.com/user/emails")
                .headers(headers -> headers.setBearerAuth(accessToken.getTokenValue()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

    }
}
