package com.example.springbootlabmessages.WebControllers;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Controller
public class MyPageController {

    private final UserService userService;
    private final MessageService messageService;

    public MyPageController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }


    // MYPAGE
    @GetMapping("/mypage")
    String mypage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().findFirst().get().getAuthority().equals("OAUTH2_USER")){
            var user = userService.findById(principal.getAttribute("id"));
            model.addAttribute("user", user);
            model.addAttribute("userdata", new UserFormData());
            model.addAttribute("principal", principal);
            return "mypage";
        }
        else {
            return "redirect:/login";
        }

    }

    @PostMapping("/mypage")
    String updateUser(@AuthenticationPrincipal OAuth2User principal, @ModelAttribute User user) {
        var nyUser = userService.findById(principal.getAttribute("id"));
        userService.updateUser(nyUser, user);
        return "redirect:/mypage";
    }


    @GetMapping("/mymessages")
    String myMessages(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2User principal = authentication.getPrincipal();
        model.addAttribute("principal", principal);
        OAuth2User oAuth2User = authentication.getPrincipal();
        Long userId = userService.findById(oAuth2User.getAttribute("id")).getId();
        List<Message> messageList = messageService.getAllMessagesByUser(userId);
        model.addAttribute("messageList", messageList);
        return "MyPage/mymessages";
    }


}
