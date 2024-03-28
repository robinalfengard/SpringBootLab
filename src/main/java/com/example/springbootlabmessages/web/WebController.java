package com.example.springbootlabmessages.web;

import com.example.springbootlabmessages.Message.Message;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.User.User;
import com.example.springbootlabmessages.User.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class WebController {
    private final MessageService messageService;
    private final UserService userService;

    public WebController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/createmessage")
    String home() {
        return "createmessage";
    }
    
    @GetMapping("/")
    String getMessages(Model model){
        var listOfMessages =messageService.getAllMessages();
        model.addAttribute("listOfMessages", listOfMessages);
        return "messages";
    }

    @PostMapping("/createmessage")
    public ResponseEntity<Void> createMessage (@RequestBody Message message){
        messageService.save(message);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create("/createmessage"))
                .build();
    }
}
