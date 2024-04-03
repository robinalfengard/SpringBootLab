package com.example.springbootlabmessages.Message;

import jakarta.transaction.Transactional;
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

    @Transactional
    @CacheEvict(value = "message", allEntries = true)
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Transactional
    public Message updateMessage(Long id, String title, String text) {
        Message message = findById(id);
        if (message != null) {
            message.setTitle(title);
            message.setText(text);
            save(message);
        }
        return message;
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
            System.out.println("Return message: " + optionalMessage.get().getTitle());
            return optionalMessage.get();
        } else {
            return null;
        }
    }
}
