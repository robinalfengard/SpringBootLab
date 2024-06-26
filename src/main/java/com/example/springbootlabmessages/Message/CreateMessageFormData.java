package com.example.springbootlabmessages.Message;

import com.example.springbootlabmessages.User.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateMessageFormData {

    private String title;
    private String text;
    private User user;
    private boolean isPublic;
    private LocalDateTime localDateTime;

    public CreateMessageFormData() {
    }

    public Message toEntity() {
        var message = new Message();
        message.setTitle(title);
        message.setText(text);
        message.setUser(user);
        message.setIsPublic(isPublic);
        message.setLastEditedBy(user);
        return message;
    }


}
