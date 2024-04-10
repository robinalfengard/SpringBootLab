package com.example.springbootlabmessages.Message;

import com.example.springbootlabmessages.User.User;
import com.example.springbootlabmessages.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;




    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
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
    public void updateMessage(Message oldMessage, Message newMessage, User user) {
        if (user != null) {
            oldMessage.setTitle(newMessage.getTitle());
            oldMessage.setText(newMessage.getText());
            oldMessage.setLastEditedBy(user);
            messageRepository.save(oldMessage);
        }
    }


    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getAllMessagesByUser(Long id) {
        return messageRepository.findAllByUserId(id);
    }


    public List<Message> get1PublicMessage(int messageLimitPerLoad) {
        return messageRepository.find10NextPublicMessages(messageLimitPerLoad);
    }

    public List<Message> get1Message(int messagesPerLoad) {
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


    public List<Message> findAllByUserName(String username) {
        System.out.println("Username in service: " + username);
        List<User> usersList = userRepository.findAllByUsernameWildcard(username);
        List<Message> messageList = new ArrayList<>();
        for(User u : usersList){
            System.out.println(u.getId());
            messageList.addAll(messageRepository.findAllByUserId(u.getId()));
        }
        return messageList;
    }

    public List<Message> filterPublicMessages(List<Message> messageList) {
        return messageList.stream().filter(Message::isPublic).toList();
    }
}
