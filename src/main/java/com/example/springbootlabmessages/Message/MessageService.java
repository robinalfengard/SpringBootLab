package com.example.springbootlabmessages.Message;

import ch.qos.logback.core.joran.event.BodyEvent;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public List<Message> getAllMessagesByUserName(String username) {
        return messageRepository.findAllByUserName(username);
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId).get();
    }

    public Message getMessageById(Long messageId) {
        return messageRepository.findById(messageId).get();
    }

    public void updateMessageText(Long messageId, String translatedMessage) {
        Message message = messageRepository.findById(messageId).get();
        message.setText(translatedMessage);
        messageRepository.save(message);
    }
}
