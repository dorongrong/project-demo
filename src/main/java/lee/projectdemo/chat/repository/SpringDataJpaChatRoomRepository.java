package lee.projectdemo.chat.repository;

import lee.projectdemo.chat.domain.ChatRoom;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.login.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findByItemAndSellerAndBuyer(Item item, User seller, User buyer);

    //판매자가 채팅방을 가져올때
    ChatRoom findByItemAndSellerAndBuyerId(Item item, User seller, Long buyerId);

    // 기존에 같은 buyer와 item이 존재하는지 확인하는 쿼리 메소드
    boolean existsByBuyerAndSellerAndItem(User buyer, User seller, Item item);

    boolean existsByBuyerIdAndSellerAndItem(Long buyerId, User seller, Item item);



}
