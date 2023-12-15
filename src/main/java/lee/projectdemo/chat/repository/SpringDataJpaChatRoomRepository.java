package lee.projectdemo.chat.repository;

import lee.projectdemo.chat.domain.ChatRoom;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.login.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 기존에 같은 buyer와 item이 존재하는지 확인하는 쿼리 메소드
    boolean existsByBuyerAndItem(User buyer, Item item);

}
