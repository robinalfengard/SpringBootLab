package com.example.springbootlabmessages.web;
import com.example.springbootlabmessages.Message.CreateMessageFormData;
import com.example.springbootlabmessages.Message.Message;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.Translation.Language;
import com.example.springbootlabmessages.Translation.LanguageDTO;
import com.example.springbootlabmessages.Translation.TranslationService;
import com.example.springbootlabmessages.User.User;
import com.example.springbootlabmessages.User.UserFormData;
import com.example.springbootlabmessages.User.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private int messagesPerLoad = 1;

    private Long currentMessageToTranslateId;
    private String currentTranslation;

    private final MessageService messageService;
    private final UserService userService;

    private final TranslationService translationService;


    public WebController(MessageService messageService, UserService userService, TranslationService translationService) {
        this.messageService = messageService;
        this.userService = userService;
        this.translationService = translationService;
    }


    @GetMapping("/createmessage")
    String home(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2User principal = authentication.getPrincipal();
        model.addAttribute("formdata", new CreateMessageFormData());
        model.addAttribute("principal", principal);
        return "createmessage";
    }

    @PostMapping("/createmessage")
    public String createMessage(CreateMessageFormData message, OAuth2AuthenticationToken authentication) throws JsonProcessingException {
        OAuth2User principal = authentication.getPrincipal();
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().findFirst().get().getAuthority().equals("OAUTH2_USER")) {
            message.setUser(userService.findById(principal.getAttribute("id")));
            var messageToSave = message.toEntity();
            System.out.println(messageToSave.toString());
            messageService.save(messageToSave);
        } else {
            System.out.println("User not found");
        }
        return "redirect:/";
    }



    @GetMapping("/")
    String getMessages(Model model, Language language, LanguageDTO selectedLang, String userName) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("lang", language);
        model.addAttribute("principal", auth);
        model.addAttribute("username", username);
        List<Language> languagesList = List.of(Language.values());
        model.addAttribute("languagesList", languagesList);
        model.addAttribute("selectedLang", selectedLang);
        model.addAttribute("translatedMessage", currentTranslation);
        model.addAttribute("currentMessageToTranslateId", currentMessageToTranslateId);
        if (auth.getAuthorities().stream().findFirst().get().getAuthority().equals("OAUTH2_USER")) {
            var listOfMessages = messageService.get10Messages(messagesPerLoad);
            model.addAttribute("listOfMessages", listOfMessages);
            return "messages";
        } else {
            var listOfMessages = messageService.get10PublicMessages(messagesPerLoad);
            model.addAttribute("listOfMessages", listOfMessages);
            return "allMessages";
        }
    }

    @GetMapping("/loadMoreMessages")
    public String loadMoreMessages(Model model, Language language, LanguageDTO selectedLang) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("lang", language);
        model.addAttribute("selectedLang", selectedLang);

        if (auth != null && auth.isAuthenticated()) {
            List<Language> languagesList = List.of(Language.values());
            model.addAttribute("languagesList", languagesList);
            messagesPerLoad += 1;
            var listOfMessages = messageService.get10Messages(messagesPerLoad);
            model.addAttribute("listOfMessages", listOfMessages);
        } else {
            messagesPerLoad += 1;
            var listOfMessages = messageService.get10PublicMessages(messagesPerLoad);
            model.addAttribute("listOfMessages", listOfMessages);
        }
        return "redirect:/";
    }





    @GetMapping("/mypage")
    String mypage(@AuthenticationPrincipal OAuth2User principal, Model model) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(SecurityContextHolder.getContext().getAuthentication().toString());
        if(auth != null || auth.isAuthenticated()){
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
        nyUser.setName(user.getName());
        nyUser.setEmail(user.getEmail());
        nyUser.setProfilePicture(user.getProfilePicture());
        nyUser.setProfilePictureBytes(user.getProfilePictureBytes());
        nyUser.setUsername(user.getUsername());
        userService.save(nyUser);
        return "redirect:/mypage";
    }


    @GetMapping("/mymessages")
    String myMessages(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2User principal = authentication.getPrincipal();
        Long userId = userService.findById(principal.getAttribute("id")).getId();
        List<Message> messageList = messageService.getAllMessagesByUser(userId);
        model.addAttribute("messageList", messageList);
        return "mymessages";
    }


    @GetMapping("/editmessage/{id}")
    public String editMessage(Model model, @PathVariable Long id) {
        model.addAttribute("message", messageService.findById(id));
        return "editmessage";
    }

    @PostMapping("/editmessage/{id}")
    public String updateMessage(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id, @ModelAttribute Message message) {
        var user = userService.findById(principal.getAttribute("id"));
        var oldMessage = messageService.findById(id);
        oldMessage.setTitle(message.getTitle());
        oldMessage.setText(message.getText());
        oldMessage.setLastEditedBy(user);
        messageService.save(oldMessage);
        return "redirect:/mymessages";
    }

    @PostMapping("/translate/{messageId}")
    public String translateMessage(@PathVariable Long messageId, @ModelAttribute LanguageDTO selectedLang, @ModelAttribute Message message, Model model) throws JsonProcessingException {
        String messageToTranslate = messageService.getMessageById(messageId).getText();
        currentTranslation = translationService.translate(messageToTranslate, selectedLang.getLangCode());
        currentMessageToTranslateId = messageId;
        return "redirect:/";
    }




    @PostMapping("/search/{username}")
    public String searchMessages(@PathVariable String username, Model model) {
        System.out.println("Username in webcontroller: " + username);
        List<Message> messageList = messageService.findAllByUserName(username);

        if(!messageList.isEmpty())
            messageList.forEach(message -> System.out.println(message.getText()));
        else
            System.out.println("Empty list");
        model.addAttribute("messageList", messageList);
        return "redirect:/";
    }
}





