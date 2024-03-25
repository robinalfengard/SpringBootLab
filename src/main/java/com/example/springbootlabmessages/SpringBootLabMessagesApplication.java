package com.example.springbootlabmessages;

import com.example.springbootlabmessages.User.User;
import com.example.springbootlabmessages.User.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBootLabMessagesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLabMessagesApplication.class, args);
    }


    @Bean
    ApplicationRunner insertDummyData(UserRepository userRepository) {
        return args -> {
            var result = userRepository.findByUsername("Knatte");
            if (result == null) {
                userRepository.save(new User("Knatte", "test", "test", "test", "test"));
            }
        };
    }
}
