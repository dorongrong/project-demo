package lee.projectdemo.chat.repository;

import lee.projectdemo.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
