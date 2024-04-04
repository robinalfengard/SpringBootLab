package com.example.springbootlabmessages.Message;

import com.example.springbootlabmessages.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message")
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean isPublic;


    private LocalDateTime timestamp;
    @Column(length = 10000)
    private String text;
    private String title;
    private String content;

    public Message() {
    }

    public Message(String text, String title, User user, boolean isPublic) {
        this.text = text;
        this.title = title;
        this.user = user;
        this.isPublic = isPublic;
        this.timestamp = LocalDateTime.now();
    }

    // I CreateMessageFormData could not change ispublic, because it has been created manually.
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}