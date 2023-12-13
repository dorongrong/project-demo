package lee.projectdemo.chat.repository;

import lee.projectdemo.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaChatRepository extends JpaRepository<Chat, Long> {
}
