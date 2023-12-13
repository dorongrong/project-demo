package lee.projectdemo.chat.domain;


import jakarta.persistence.*;
import lee.projectdemo.item.item.Item;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    //queueName에서 구매자의 정보를 추출하자
    @Column(unique=true)
    private String queueName;

    //item에서 판매자 정보 추출
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    //채팅이랑 매핑 같은 생명주기 공유
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    public ChatRoom() {
    }
}
