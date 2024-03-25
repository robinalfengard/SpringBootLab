package com.example.springbootlabmessages.Message;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
