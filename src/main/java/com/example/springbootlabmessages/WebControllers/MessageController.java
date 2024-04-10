package com.example.springbootlabmessages.WebControllers;

import com.example.springbootlabmessages.Message.CreateMessageFormData;
import com.example.springbootlabmessages.Message.Message;
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
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;

    public MessageController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    // CREATE MESSAGES
    @GetMapping("/createmessage")
    String home(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2User principal = authentication.getPrincipal();
        model.addAttribute("formdata", new CreateMessageFormData());
        model.addAttribute("principal", principal);
        return "createmessage";
    }

    @PostMapping("/createmessage")
    public String createMessage(CreateMessageFormData message, OAuth2AuthenticationToken authentication) {
        OAuth2User principal = authentication.getPrincipal();
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().findFirst().get().getAuthority().equals("OAUTH2_USER")) {
            message.setUser(userService.findById(principal.getAttribute("id")));
            var messageToSave = message.toEntity();
            messageService.save(messageToSave);
        }
        return "redirect:/";
    }


    // Edit Messages
    @GetMapping("/editmessage/{id}")
    public String editMessage(Model model, @PathVariable Long id, OAuth2AuthenticationToken authentication) {
        OAuth2User principal = authentication.getPrincipal();
        model.addAttribute("principal", principal);
        model.addAttribute("message", messageService.findById(id));
        return "editmessage";
    }

    @PostMapping("/editmessage/{id}")
    public String updateMessage(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id, @ModelAttribute Message message) {
        var user = userService.findById(principal.getAttribute("id"));
        var oldMessage = messageService.findById(id);
        messageService.updateMessage(oldMessage, message, user);
        return "redirect:/mymessages";
    }
}
