package com.example.springbootlabmessages.WebControllers;

import com.example.springbootlabmessages.GetRandomInfoApi.GetRandomInfoService;
import com.example.springbootlabmessages.Message.CreateMessageFormData;
import com.example.springbootlabmessages.Message.Message;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.User.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
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

    private final GetRandomInfoService getRandomInfoService;

    public MessageController(UserService userService, MessageService messageService, GetRandomInfoService getRandomInfoService) {
        this.userService = userService;
        this.messageService = messageService;
        this.getRandomInfoService = getRandomInfoService;
    }

    // CREATE MESSAGES
    @GetMapping("/createmessage")
    public String home(OAuth2AuthenticationToken authentication, Model model, HttpSession session) throws JsonProcessingException {
        System.out.println(session.getAttribute("randomPhrase"));
        OAuth2User principal = authentication.getPrincipal();
        model.addAttribute("formdata", new CreateMessageFormData());
        model.addAttribute("principal", principal);
        return "CreateMessage/createmessage";
    }

    @GetMapping("/newRandomPhrase")
    public String newRandomPhrase(OAuth2AuthenticationToken authentication, HttpSession session, Model model) throws JsonProcessingException {
        OAuth2User principal = authentication.getPrincipal();
        model.addAttribute("principal", principal);
        String randomPhrase = getRandomInfoService.getRandomInfo();
        session.setAttribute("randomPhrase", randomPhrase);
        model.addAttribute("formdata", new CreateMessageFormData());
        model.addAttribute("randomPhrase", session.getAttribute("randomPhrase"));
        return "CreateMessage/createmessage";
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
        return "MyPage/editmessage";
    }

    @PostMapping("/editmessage/{id}")
    public String updateMessage(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id, @ModelAttribute Message message) {
        var user = userService.findById(principal.getAttribute("id"));
        var oldMessage = messageService.findById(id);
        messageService.updateMessage(oldMessage, message, user);
        return "redirect:/mymessages";
    }
}
