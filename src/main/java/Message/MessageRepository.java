package Message;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface MessageRepository extends ListCrudRepository<Message, Long> {

    List<Message> findAllByUserId(Long userId);

}
