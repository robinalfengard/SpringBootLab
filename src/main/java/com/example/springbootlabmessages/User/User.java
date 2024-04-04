package com.example.springbootlabmessages.User;

import com.example.springbootlabmessages.Message.Message;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Setter
@Getter
@Entity
public class User {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String username;
    private String name;
    private String email;
    private String url;
    private String login;
    private String profilePicture;
    @Lob
    private byte[] profilePictureBytes;

    public User() {}

    public User(Long id , String username, String name, String email, String profilePicture) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
        this.profilePictureBytes = null;
    }

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    List<Message> messageList = new ArrayList<>();
//    static String convertImageToDataURL(byte[] imageData,String fileName) {
//        try {
//            // Konvertera byte-array till en Base64-kodad sträng
//            String base64Encoded = Base64.getEncoder().encodeToString(imageData);
//
//            Path resourceDirectory = Paths.get("src/main/resources");
//            String absolutePath = resourceDirectory.toFile().getAbsolutePath();
//
//            // Skapa en sökväg till den nya bilden
//            Path imagePath = Paths.get(absolutePath, fileName);
//
//            // Dekodera Base64-strängen och skriv till filen
//            Files.write(imagePath, Base64.getDecoder().decode(base64Encoded));
//
//            // Returnera URL:en till den sparade bilden
//            return imagePath.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null; // Returnera null om det uppstår ett fel
//        }
//    }

//    public String getProfilePicture() {
//        return profilePicture!= null ? profilePicture : convertImageToDataURL(profilePictureBytes,"profilepicture");
//    }
}