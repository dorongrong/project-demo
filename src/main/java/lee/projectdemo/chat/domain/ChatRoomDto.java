package lee.projectdemo.chat.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRoomDto {

    private Long itemId;
    //채팅 요구자를 의미 loginId임
    private String senderId;
    private String displayName;
    private String itemName;

    private Long buyerId;

    private List<Chat> chats = new ArrayList<>();

    public ChatRoomDto() {
    }

    public ChatRoomDto(Long itemId, String displayName, String itemName, Long buyerId) {
        this.itemId = itemId;
        this.displayName = displayName;
        this.itemName = itemName;
        this.buyerId = buyerId;
    }

    public ChatRoomDto(Long itemId, String senderId, String displayName, String itemName, List<Chat> chats) {
        this.itemId = itemId;
        this.senderId = senderId;
        this.displayName = displayName;
        this.itemName = itemName;
        this.chats = chats;
    }
}
