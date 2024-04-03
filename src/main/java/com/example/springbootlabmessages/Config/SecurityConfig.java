package com.example.springbootlabmessages.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestClient;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/", "/resources/**", "/type/css/**", "createMessage.css","mymessages.css", "home.css", "/api/**", "/loadMorePublicMessages", "/translate/**", "/translate").permitAll();
                    auth.requestMatchers("/updateuser","/createmessage", "/allMessages","/mymessages", "/mypage", "/loadMoreMessages").authenticated();
                    auth.anyRequest().denyAll();
                })
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .successHandler(successHandler()))

                .build();
    }


    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> response.sendRedirect("/createmessage");
    }


    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER\n" +
                "ROLE_USER > ROLE_GUEST");
        return hierarchy;
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }




}