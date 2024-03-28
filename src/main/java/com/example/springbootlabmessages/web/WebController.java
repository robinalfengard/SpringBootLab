package com.example.springbootlabmessages.web;

import com.example.springbootlabmessages.Message.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    private final MessageService messageService;

    public WebController(MessageService messageService) {
        this.messageService = messageService;
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
        

}
