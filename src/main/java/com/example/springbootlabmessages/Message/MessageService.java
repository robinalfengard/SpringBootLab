package com.example.springbootlabmessages.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
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

    public Message findById(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    public Message update(Message message) {
        Optional<Message> existingMessageOpt = messageRepository.findById(message.getId());
        if (existingMessageOpt.isPresent()) {
            Message existingMessage = existingMessageOpt.get();
            existingMessage.setTitle(message.getTitle());
            existingMessage.setContent(message.getContent());
            // update other fields as necessary
            return messageRepository.save(existingMessage);
        } else {
            throw new IllegalArgumentException("Message with id " + message.getId() + " does not exist");
        }
    }
}
