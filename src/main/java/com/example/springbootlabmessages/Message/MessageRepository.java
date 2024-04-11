package com.example.springbootlabmessages.Message;

import com.example.springbootlabmessages.User.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface MessageRepository extends ListCrudRepository<Message, Long> {

    List<Message> findAllByUserId(Long userId);

    @Query("SELECT m FROM Message m WHERE m.user.username = ?1")
    List<Message> findAllByUserName(String userName);

    @Query("SELECT m FROM Message m WHERE m.isPublic = true")
    List<Message> findAllWhereIsPublicIsTrue();


    @Query(value = "SELECT * FROM message WHERE is_public = true ORDER BY created_at DESC LIMIT :messageLimitPerLoad", nativeQuery = true)
    List<Message> find10NextPublicMessages(int messageLimitPerLoad);

    @Query(value = "SELECT * FROM message ORDER BY created_at DESC LIMIT :messageLimitPerLoad", nativeQuery = true)
    List<Message> find10NextMessages(int messageLimitPerLoad);


}
