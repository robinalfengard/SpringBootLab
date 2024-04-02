package com.example.springbootlabmessages.Message;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface MessageRepository extends ListCrudRepository<Message, Long> {

    List<Message> findAllByUserId(Long userId);

    @Query("SELECT m FROM Message m WHERE m.isPublic = true")
    List<Message> findAllWhereIsPublicIsTrue();

    @Query("SELECT m FROM Message m WHERE m.user.id = ?1")
    List<Message> findAllByUser(Long userId);


}
