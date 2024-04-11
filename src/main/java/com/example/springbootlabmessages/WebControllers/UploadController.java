package com.example.springbootlabmessages.web;

import com.example.springbootlabmessages.User.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
public class UploadController {
    UserService userService;
    @Value("${user.home}")
    String UPLOAD_DIRECTORY ;

    public UploadController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/uploadimage")
    public String displayUploadForm() {
        return "imageupload";
    }

    @PostMapping("/upload") public String uploadImage(Model model, @RequestParam("image") MultipartFile file, OAuth2AuthenticationToken authentication) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY+"/uploads/profilepics", file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        OAuth2User principal = authentication.getPrincipal();
        var nyUser = userService.findById(principal.getAttribute("id"));
        nyUser.setProfilePicture("/profilepics/" + file.getOriginalFilename());
        userService.save(nyUser);
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        return "MyPage/imageupload";
    }
}
