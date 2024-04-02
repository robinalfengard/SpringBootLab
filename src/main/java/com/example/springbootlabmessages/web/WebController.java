package com.example.springbootlabmessages.web;

import com.example.springbootlabmessages.Message.CreateMessageFormData;
import com.example.springbootlabmessages.Message.Message;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.User.User;
import com.example.springbootlabmessages.User.UserFormData;
import com.example.springbootlabmessages.User.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    private  int messagesPerLoad = 1;

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
        messagesPerLoad = 1;
        var listOfMessages = messageService.get10PublicMessages(messagesPerLoad);
        model.addAttribute("listOfMessages", listOfMessages);
        return "messages";
    }
    @GetMapping("/allMessages")
    String getLoggedInMessages(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            messagesPerLoad = 1;
            var listOfMessages = messageService.get10Messages(messagesPerLoad);
            model.addAttribute("listOfMessages", listOfMessages);
            return "allMessages";
        } else {
            System.out.println("User not authenticated");
            // Handle tsout he case when the user is not authenticated
            return "redirect:/login";
        }
    }

    @GetMapping("/loadMorePublicMessages")
    public String loadMorePublicMessages(Model model){
        messagesPerLoad +=1;
        var listOfMessages =messageService.get10PublicMessages(messagesPerLoad);
        model.addAttribute("listOfMessages", listOfMessages);
        return "messages";
    }

    @GetMapping("/loadMoreMessages")
    public String loadMoreMessages(Model model){
        messagesPerLoad +=1;
        var listOfMessages =messageService.get10Messages(messagesPerLoad);
        model.addAttribute("listOfMessages", listOfMessages);
        return "allMessages";
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
    @GetMapping("/mypage")
    String mypage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        var user = userService.findById(principal.getAttribute("id"));
        model.addAttribute("user", user);
        model.addAttribute("userdata", new UserFormData());
        model.addAttribute("principal", principal);
        return "mypage";
    }

    @PutMapping("/mypage")
    String updateUser(@AuthenticationPrincipal OAuth2User principal,@ModelAttribute User userdata) {
        var user = userService.findById(principal.getAttribute("id"));
        user.setName(userdata.getName());
        user.setEmail(userdata.getEmail());
        user.setProfilePicture(userdata.getProfilePicture());
        user.setUsername(userdata.getUsername());
        userService.save(user);
        return "redirect:/mypage";
    }

    @GetMapping("/mymessages")
    String myMessages(@AuthenticationPrincipal OAuth2User principal, Model model) {
        Object idObject = principal.getAttribute("id");
        Long id;
        if (idObject instanceof Integer) {
            id = ((Integer) idObject).longValue();
        } else if (idObject instanceof Long) {
            id = (Long) idObject;
        } else {
            throw new IllegalArgumentException("ID is not of type Integer or Long");
        }
        List<Message> messageList = messageService.getAllMessagesByUser(id);
        model.addAttribute("messageList", messageList);
        return "mymessages";
    }
}

