package com.example.springbootlabmessages.Message;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllPublicMessages() {
        return messageRepository.findAllWhereIsPublicIsTrue();
    }

    @CacheEvict(value = "message", allEntries = true)
    public void save(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getAllMessagesByUser(Long id) {
        return messageRepository.findAllByUserId(id);
    }


    public List<Message> get10PublicMessages(int messageLimitPerLoad) {
        return messageRepository.find10NextPublicMessages(messageLimitPerLoad);
    }

    public List<Message> get10Messages(int messagesPerLoad) {
        return messageRepository.find10NextMessages(messagesPerLoad);
    }
}
