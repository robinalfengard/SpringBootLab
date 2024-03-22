package com.example.springbootlabmessages.Message;

import com.example.springbootlabmessages.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String text;
    private String title;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean isPublic;

    public Message() {
    }

    public Message(Long id, String text, String title, LocalDateTime timestamp, User user, boolean isPublic) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.timestamp = timestamp;
        this.user = user;
        this.isPublic = isPublic;
    }
}