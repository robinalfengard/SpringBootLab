package com.example.springbootlabmessages.rest;

import com.example.springbootlabmessages.Message.CreateMessageFormData;
import com.example.springbootlabmessages.Message.Message;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
public class MessageRestController {

    private final MessageService messageService;
    private final UserService userService;


    public MessageRestController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

//    @GetMapping("/messages")
//    public ResponseEntity<List<Message>> getMessages() {
//        var listOfMessages = messageService.getAllMessages();
//        return ResponseEntity.ok(listOfMessages);
//    }

    @GetMapping("/messages")
    public List<Message> messages() {
        var messages = messageService.getAllMessages().stream()
                .peek(message -> message.setText(HtmlUtils.htmlEscape(message.getText())))
                .toList();
        return messages;
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody CreateMessageFormData messageData, @AuthenticationPrincipal OAuth2User principal) {
        if (principal.getAttribute("id") != null && userService.findById(principal.getAttribute("id")) != null) {
            messageData.setUser(userService.findById(principal.getAttribute("id")));
            var messageToSave = messageData.toEntity();
           messageService.save(messageToSave);
            return ResponseEntity.status(201).body(messageToSave);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
