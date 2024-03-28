package com.example.springbootlabmessages.web;

import com.example.springbootlabmessages.Message.CreateMessageFormData;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.User.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    private final MessageService messageService;
    private final UserService userService;


    public WebController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }



    @GetMapping("/createmessage")
    String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("formdata", new CreateMessageFormData());
        model.addAttribute("principal", principal);
        return "createmessage";
    }
    
    @GetMapping("/")
    String getMessages(Model model){
        var listOfMessages =messageService.getAllPublicMessages();
        model.addAttribute("listOfMessages", listOfMessages);
        return "messages";
    }
    @GetMapping("/loggedInMessages")
    String getLoggedInMessages(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            var listOfMessages = messageService.getAllMessages();
            model.addAttribute("listOfMessages", listOfMessages);
            return "allMessages";
        } else {
            System.out.println("User not authenticated");
            // Handle tsout he case when the user is not authenticated
            return "redirect:/login";
        }
    }

    @PostMapping("/createmessage")
    public String createMessage(CreateMessageFormData message, OAuth2AuthenticationToken authentication) {
        OAuth2User principal = authentication.getPrincipal();
        if(userService.findById(principal.getAttribute("id")) != null){
            message.setUser(userService.findById(principal.getAttribute("id")));
            var messageToSave = message.toEntity();
            System.out.println(messageToSave.toString());
            messageService.save(messageToSave);
        } else {
            System.out.println("User not found");
        }
        return "redirect:/allMessages";
    }
/*        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create("/"))
                .build();*/
}

