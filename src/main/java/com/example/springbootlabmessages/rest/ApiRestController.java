package com.example.springbootlabmessages.rest;

import com.example.springbootlabmessages.Message.Message;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.User.User;
import com.example.springbootlabmessages.User.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ApiRestController {

    private final MessageService messageService;
    private final UserService userService;


    public ApiRestController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/messages")
    public List<MessageDto> messages3() {
        return messageService.getAllPublicMessages().stream()
                .map(message -> new MessageDto(message.getUser().getUsername(), message.getText()))
                .toList();
    }
    public record MessageDto(String Username, String message) {
    }

    @GetMapping("/messages/{userId}")
    public List<String> messagesByUser(@PathVariable String userId) {
        return messageService.getAllMessagesByUser(Long.valueOf(userId)).stream()
                .map(Message::getText)
                .toList();
    }

    @GetMapping("/messages/user/{username}")
    public List<String> message(@PathVariable String username) {
        return messageService.getAllMessagesByUserName(username).stream()
                .map(Message::getText)
                .toList();
    }

    @GetMapping("/users")
    public List<String> users() {
        return userService.getAllUsers();
    }

}
