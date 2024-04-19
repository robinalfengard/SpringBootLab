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
    private final UserRepository userRepository;


    public HtmxController(MessageRepository messageRepository, UserService userService, MessageService messageService,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    // main page
    @GetMapping("/htmx")
    public String htmx(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("formData", new CreateMessageFormData());
        model.addAttribute("principal", auth);
        model.addAttribute("userId", Long .valueOf(auth.getName()));
        if(auth.getAuthorities().stream().findFirst().get().getAuthority().equals("OAUTH2_USER"))
            model.addAttribute("listOfMessages", messageRepository.findAll());
        else
            model.addAttribute("listOfMessages", messageRepository.findAllWhereIsPublicIsTrue());
        return "htmx";
    }

    // selected message
    @GetMapping("/htmx/{id}")
    public String getMessageById(@PathVariable Long id, Model model) {
        var message = messageRepository.findById(id).get();
        model.addAttribute("message", message);
        model.addAttribute("updatedMessage", new CreateMessageFormData());
        model.addAttribute("id", id);
        System.out.println("Get specific message with id: " + id);
        return "htmx-edit";
    }


    // edit message
    @PostMapping("/htmx/{id}")
    public String editMessage(@PathVariable Long id, CreateMessageFormData updatedMessage, Model model) {
        model.addAttribute("updatedMessage", updatedMessage);
        Message update = updatedMessage.toEntity();
        System.out.println(update.getText());
        System.out.println(id);
    //    messageService.updateMessage(oldMessage, update, userRepository.findById(id).get());
        return "htmx";
    }

    // create new message form
    @GetMapping("/htmx/postform")
    public String getPostForm(Model model) {
        model.addAttribute("newMessage", new CreateMessageFormData());
        model.addAttribute("userId", SecurityContextHolder.getContext().getAuthentication().getName());
        return "htmx-post";
    }




    // delete method
    @ResponseBody
    @DeleteMapping(value = "/htmx/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteMessage(@PathVariable Long id) {
        System.out.println("Delete message with id: " + id);
        messageRepository.deleteById(id);
        return "";
    }

}
