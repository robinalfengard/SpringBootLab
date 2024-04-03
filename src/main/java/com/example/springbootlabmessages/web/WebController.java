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

import java.time.LocalDateTime;
import java.util.List;

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
    String getMessages(Model model) {
        var listOfMessages = messageService.getAllPublicMessages();
        model.addAttribute("listOfMessages", listOfMessages);
        return "messages";
    }

    @GetMapping("/allMessages")
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
        if (userService.findById(principal.getAttribute("id")) != null) {
            message.setUser(userService.findById(principal.getAttribute("id")));
            var messageToSave = message.toEntity();
            System.out.println(messageToSave.toString());
            messageService.save(messageToSave);
        } else {
            System.out.println("User not found");
        }
        return "redirect:/allMessages";
    }

    @GetMapping("/mypage")
    String mypage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        var user = userService.findById(principal.getAttribute("id"));
        model.addAttribute("user", user);
        model.addAttribute("userdata", new UserFormData());
        model.addAttribute("principal", principal);
        return "mypage";
    }

    @PutMapping("/mypage")
    String updateUser(@AuthenticationPrincipal OAuth2User principal, @ModelAttribute User userdata) {
        var user = userService.findById(principal.getAttribute("id"));
        user.setName(userdata.getName());
        user.setEmail(userdata.getEmail());
        user.setProfilePicture(userdata.getProfilePicture());
        user.setUsername(userdata.getUsername());
        userService.save(user);
        return "redirect:/mypage";
    }

    @GetMapping("/mymessages")
    String myMessages(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2User principal = authentication.getPrincipal();
        // Object idObject = principal.getAttribute("id");
        Long userId = userService.findById(principal.getAttribute("id")).getId();
/*        Long id;
        if (idObject instanceof Integer) {
            id = ((Integer) idObject).longValue();
        } else if (idObject instanceof Long) {
            id = (Long) idObject;
        } else {
            throw new IllegalArgumentException("ID is not of type Integer or Long");
        }*/
        List<Message> messageList = messageService.getAllMessagesByUser(userId);
        model.addAttribute("messageList", messageList);
        return "mymessages";
    }

    @GetMapping("/editmessage/{id}")
    public String editMessage(OAuth2AuthenticationToken authentication, Model model, @PathVariable Long id) {
        Message existingMessage = messageService.findById(id);
        Message message = messageService.findById(existingMessage.getId());
        model.addAttribute("message", message);
        return "editmessage";
    }

/*    @PostMapping("/editmessage")
    public String updateMessage (OAuth2AuthenticationToken authentication, @ModelAttribute Message updatedMessage, Model model){
        OAuth2User principal = authentication.getPrincipal();
        Long userId = userService.findById(principal.getAttribute("id")).getId();
        updatedMessage.setId(userId);
        messageService.save(updatedMessage);
        return "redirect:/mymessages";
    }*/

    /*@PatchMapping("/editmessage/{id}")
    public String updateMessagePatch(@PathVariable("id") Long id, @ModelAttribute Message updatedMessage, Model model) {
        Message existingMessage = messageService.findById(id);
        System.out.println(existingMessage.getUser().getName());
        updatedMessage.setUser(existingMessage.getUser());
        existingMessage.setTimestamp(LocalDateTime.now());
        existingMessage.setTitle(updatedMessage.getTitle());
        existingMessage.setText(updatedMessage.getText());
        messageService.save(existingMessage);
        return "redirect:/mymessages";
    }*/

    @PostMapping("/editmessage/{id}")
    public String updateMessage(OAuth2AuthenticationToken authentication, @PathVariable Long id, Model model, Message updatedMessage) {
        OAuth2User principal = authentication.getPrincipal();
        Message existingMessage = messageService.findById(id);
        model.addAttribute("message", updatedMessage);
        if (userService.findById(principal.getAttribute("id")) != null) {
            existingMessage.setTitle(updatedMessage.getTitle());
            existingMessage.setText(updatedMessage.getText());
            messageService.save(existingMessage);
        }
        return "redirect:/mymessages";
    }
}