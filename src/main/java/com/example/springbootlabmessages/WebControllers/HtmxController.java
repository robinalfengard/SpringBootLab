package com.example.springbootlabmessages.WebControllers;

import com.example.springbootlabmessages.Message.CreateMessageFormData;
import com.example.springbootlabmessages.Message.Message;
import com.example.springbootlabmessages.Message.MessageRepository;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.User.UserRepository;
import com.example.springbootlabmessages.User.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HtmxController {

    private final MessageRepository messageRepository;
    private final UserService userService;

    private final MessageService messageService;


    public HtmxController(MessageRepository messageRepository, UserService userService, MessageService messageService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/htmx")
    public String htmx(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("formData", new CreateMessageFormData());
        model.addAttribute("principal", auth);
        model.addAttribute("listOfMessages", messageRepository.findAll());
        System.out.println("Get method called");
        return "htmx";
    }

    @PostMapping("/htmx")
    public String createMessage(Model model, OAuth2AuthenticationToken authentication, @RequestParam("post-title") String title, @RequestParam("post-text") String message) {
        System.out.println("Post method called");
        model.addAttribute("title", title);
        model.addAttribute("message", message);
        OAuth2User principal = authentication.getPrincipal();
        var auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("principal", auth);
        System.out.println(title + " " + message);
        if (auth.getAuthorities().stream().findFirst().get().getAuthority().equals("OAUTH2_USER")) {
            CreateMessageFormData newMessage = new CreateMessageFormData();
            newMessage.setTitle(title);
            newMessage.setText(message);
            newMessage.setUser(userService.findById(principal.getAttribute("id")));
            var messageToSave = newMessage.toEntity();
            messageService.save(messageToSave);
        }
        return "htmx";
    }

    @ResponseBody
    @DeleteMapping(value = "/htmx/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteMessage(@PathVariable Long id) {
        System.out.println("Delete message with id: " + id);
        messageRepository.deleteById(id);
        return "";
    }

}
