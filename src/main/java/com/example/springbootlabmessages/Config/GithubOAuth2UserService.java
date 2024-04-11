package com.example.springbootlabmessages.Config;
import com.example.springbootlabmessages.User.User;
import com.example.springbootlabmessages.User.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



    @Service
    public class GithubOAuth2UserService extends DefaultOAuth2UserService {


        private final UserRepository userRepository;
        private final GithubService githubService;

        public GithubOAuth2UserService(UserRepository userRepository, GithubService githubService) {
            this.userRepository = userRepository;
            this.githubService = githubService;
        }


        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            OAuth2User oidcUser = super.loadUser(userRequest);
            Map<String, Object> attributes = oidcUser.getAttributes();

            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            List<Email> result = githubService.getEmails(accessToken);

            GithubUser gitHubUser = new GithubUser();
            gitHubUser.setUserId(String.valueOf(attributes.get("id")));
            gitHubUser.setName((String) attributes.get("name"));
            gitHubUser.setUrl((String) attributes.get("html_url"));
            gitHubUser.setAvatarUrl((String) attributes.get("avatar_url"));
            gitHubUser.setLogin((String) attributes.get("login"));
            gitHubUser.setEmail(result.getFirst().email());
            updateUser(gitHubUser);
            return oidcUser;
        }


        private void updateUser(GithubUser gitUser) {
        Long userId = Long.valueOf(gitUser.getUserId());
            System.out.println(userId);
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            user = new User();
        }
        user.setId(userId);
        user.setUrl(gitUser.getUrl());
        user.setName(gitUser.getName());
        user.setLogin(gitUser.getLogin());
        user.setProfilePicture(gitUser.getAvatarUrl());
        user.setUsername(gitUser.getLogin());
        user.setEmail(gitUser.getEmail());
        userRepository.save(user);
        }


    }

