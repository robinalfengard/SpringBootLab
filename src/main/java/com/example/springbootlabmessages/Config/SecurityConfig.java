package com.example.springbootlabmessages.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestClient;

import java.net.URI;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/", "/resources/**", "/type/css/**","/resources/uploads/*.*", "/resources/static/uploads/**", "createMessage.css","mymessages.css", "home.css", "/api/**", "/loadMoreMessages", "/swagger-ui/**","/swagger-ui.html", "/v3/api-docs/**", "/swagger", "/translate/**", "/translate", "/favicon.ico", "/search", "/result", "/translateSearch/**", "profilepics/**" ).permitAll();
                    auth.requestMatchers("/updateuser","/createmessage", "/allMessages","/mymessages", "/mypage","/upload" , "/uploadimage","/editmessage/**", "/newRandomPhrase").authenticated();
                    auth.anyRequest().denyAll();
                })
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .successHandler(successHandler()))
                .logout(logout -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()))
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


    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

        // Sets the `URI` that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(String.valueOf(URI.create("https://localhost:8080/")));

        return oidcLogoutSuccessHandler;
    }


}