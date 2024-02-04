package lee.projectdemo.chat.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRoomDto {

    private Long itemId;
    //채팅 요구자를 의미 loginId임
    private String sendUserLoginId;
    //사용자의 Id임
    private Long sendUserId;
    private String displayName;
    //상대방의 ID
    private Long otherUserId;
    private String itemName;

    private Long buyerId;

    private List<String> imageURLs;

    private List<Chat> chats = new ArrayList<>();

    public ChatRoomDto() {
    }

    public ChatRoomDto(Long itemId, String displayName, String itemName, Long buyerId, List<String> imageURLs) {
        this.itemId = itemId;
        this.displayName = displayName;
        this.itemName = itemName;
        this.buyerId = buyerId;
        this.imageURLs = imageURLs;
    }

    public ChatRoomDto(Long itemId, String sendUserLoginId, String displayName, String itemName, List<Chat> chats, Long otherUser) {
        this.itemId = itemId;
        this.sendUserLoginId = sendUserLoginId;
        this.displayName = displayName;
        this.itemName = itemName;
        this.chats = chats;
        this.otherUserId = otherUser;
    }

    @Override
    public String toString() {
        return "ChatRoomDto{" +
                "itemId=" + itemId +
                ", sendUserLoginId='" + sendUserLoginId + '\'' +
                ", sendUserId=" + sendUserId +
                ", displayName='" + displayName + '\'' +
                ", otherUserId=" + otherUserId +
                ", itemName='" + itemName + '\'' +
                ", buyerId=" + buyerId +
                ", chats=" + chats +
                '}';
    }

}
