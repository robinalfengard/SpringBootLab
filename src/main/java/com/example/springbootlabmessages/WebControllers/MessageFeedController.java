package com.example.springbootlabmessages.WebControllers;
import com.example.springbootlabmessages.Message.Message;
import com.example.springbootlabmessages.Message.MessageService;
import com.example.springbootlabmessages.Translation.Language;
import com.example.springbootlabmessages.Translation.LanguageDTO;
import com.example.springbootlabmessages.Translation.TranslationService;
import com.example.springbootlabmessages.User.UserSearchDataDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class MessageFeedController {

    private final MessageService messageService;
    private final TranslationService translationService;


    public MessageFeedController(MessageService messageService, TranslationService translationService) {
        this.messageService = messageService;
        this.translationService = translationService;
    }

    // MESSAGE FEED
    @GetMapping("/")
    String getMessages(Model model, Language language, LanguageDTO selectedLang, HttpSession session) {
        if (session.getAttribute("messagePerLoad") == null) {
            int initialMessagePerLoad = 1;
            session.setAttribute("messagePerLoad", initialMessagePerLoad);
        }
        int currentMessagePerLoad = (Integer) session.getAttribute("messagePerLoad");
        var auth = SecurityContextHolder.getContext().getAuthentication();
        setUpLanguageModel(model, language, selectedLang);
        setUpModel(model, session);
        model.addAttribute("principal", auth);
        model.addAttribute("userSearchData", new UserSearchDataDTO());
        if (isAuthenticated(auth)) {
            var listOfMessages = messageService.get1Message(currentMessagePerLoad);
            model.addAttribute("listOfMessages", listOfMessages);
            if(session.getAttribute("messageList") != null){
                model.addAttribute("listOfMessages", session.getAttribute("messageList"));
                session.removeAttribute("messageList");
            }
        } else {
            var listOfMessages = messageService.get1PublicMessage(currentMessagePerLoad);
            model.addAttribute("listOfMessages", listOfMessages);
            if(session.getAttribute("messageList") != null){
                model.addAttribute("listOfMessages", messageService.filterPublicMessages((List<Message>) session.getAttribute("messageList")));
                session.removeAttribute("messageList");
            }
        }
        return "MessageFeed/messages";
    }



    @GetMapping("/loadMoreMessages")
    public String loadMoreMessages(Model model, Language language, LanguageDTO selectedLang, HttpSession session) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        setUpLanguageModel(model, language, selectedLang);
        session.removeAttribute("currentTranslation");
        session.removeAttribute("currentMessageToTranslateId");
        int currentMessagePerLoad = (Integer) session.getAttribute("messagePerLoad");
        session.setAttribute("messagePerLoad", currentMessagePerLoad + 1);
        System.out.println(session.getAttribute("messagePerLoad"));
        if (isAuthenticated(auth)) {
            List<Language> languagesList = List.of(Language.values());
            model.addAttribute("languagesList", languagesList);
            var listOfMessages = messageService.get1Message((Integer) session.getAttribute("messagePerLoad"));
            model.addAttribute("listOfMessages", listOfMessages);
        } else {
            var listOfMessages = messageService.get1PublicMessage((Integer) session.getAttribute("messagePerLoad"));
            model.addAttribute("listOfMessages", listOfMessages);
        }
        return "redirect:/";
    }


    // TRANSLATION
    @PostMapping("/translate/{messageId}")
    public String translateMessage(@PathVariable Long messageId, @ModelAttribute LanguageDTO selectedLang, @ModelAttribute Message message, HttpSession session) throws JsonProcessingException {
        String messageToTranslate = messageService.getMessageById(messageId).getText();
        session.setAttribute("currentTranslation", translationService.translate(messageToTranslate, selectedLang.getLangCode()));
        session.setAttribute("currentMessageToTranslateId", messageId);
        return "redirect:/";
    }
    @PostMapping("/translateSearch/{messageId}")
    public String translateSearch(@PathVariable Long messageId, @ModelAttribute LanguageDTO selectedLang, @ModelAttribute Message message, HttpSession session) throws JsonProcessingException {
        String messageToTranslate = messageService.getMessageById(messageId).getText();
        session.setAttribute("currentTranslation", translationService.translate(messageToTranslate, selectedLang.getLangCode()));
        session.setAttribute("currentMessageToTranslateId", messageId);
        return "redirect:/result";
    }



    // SEARCH
    @PostMapping("/search")
    public String searchMessages(@ModelAttribute UserSearchDataDTO searchText, HttpSession session) {
        List<Message> listOfMessagesBasedOnSearch = messageService.findAllByUserName(searchText.getSearchText());
        session.setAttribute("messageList", listOfMessagesBasedOnSearch);
        return "redirect:/result";
    }
    @GetMapping("/result")
        public String result(Model model, @ModelAttribute LanguageDTO selectedLang, Language language, HttpSession session) {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            setUpLanguageModel(model, language, selectedLang);
            setUpModel(model, session);
            if (isAuthenticated(auth)) {
                var listOfMessages = messageService.get1Message((Integer) session.getAttribute("messagePerLoad"));
                model.addAttribute("listOfMessages", listOfMessages);
                if(session.getAttribute("messageList") != null){
                    model.addAttribute("listOfMessages", session.getAttribute("messageList"));
                }
            }
            else {
                var listOfMessages = messageService.get1PublicMessage((Integer) session.getAttribute("messagePerLoad"));
                model.addAttribute("listOfMessages", listOfMessages);
                if(session.getAttribute("messageList") != null){
                    model.addAttribute("listOfMessages", messageService.filterPublicMessages((List<Message>) session.getAttribute("messageList")));
                }
            }
            return "MessageFeed/results";
        }


        // HELPER METHODS
        private void setUpModel(Model model, HttpSession session) {
            List<Language> languagesList = List.of(Language.values());
            model.addAttribute("languagesList", languagesList);
            model.addAttribute("translatedMessage", session.getAttribute("currentTranslation"));
            model.addAttribute("currentMessageToTranslateId", session.getAttribute("currentMessageToTranslateId"));
        }

        private boolean isAuthenticated(Authentication auth) {
            return auth.getAuthorities().stream().findFirst().get().getAuthority().equals("OAUTH2_USER");
        }

        private void setUpLanguageModel(Model model, Language language, LanguageDTO selectedLanguage){
            model.addAttribute("lang", language);
            model.addAttribute("selectedLang", selectedLanguage);
        }





}





