package com.example.springbootlabmessages.web;

import com.example.springbootlabmessages.User.User;
import com.example.springbootlabmessages.User.UserFormData;
import com.example.springbootlabmessages.User.UserRepository;
import com.example.springbootlabmessages.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/mypage")
    public String mypage(Model model, HttpServletRequest httpServletRequest, OAuth2AuthenticationToken authentication) {
        OAuth2User principal = authentication.getPrincipal();
        var user = userService.findById(principal.getAttribute("id"));
        model.addAttribute("user", user);
        model.addAttribute("userdata", new UserFormData());
        model.addAttribute("principal", principal);
        return "mypage";
    }

    @PostMapping("/mypage")
    String update(UserFormData userData, OAuth2AuthenticationToken authentication) {
        OAuth2User principal = authentication.getPrincipal();
        User user = userService.findById(principal.getAttribute("id"));

        // Check if the data has changed, otherwise, keep the old data
        userService.update(userData, user);

        return "redirect:/user/mypage";
    }
//    @PostMapping("/mypage")
//    String update(@RequestBody UserFormData userData, OAuth2AuthenticationToken authentication) {
//        OAuth2User principal = authentication.getPrincipal();
//        User user = userService.findById(principal.getAttribute("id"));
//        if (!user.equals(userData.toEntity())) {
//            userService.update(userData, user);
//        }
//        return "redirect:/user/mypage";
//    }

}
