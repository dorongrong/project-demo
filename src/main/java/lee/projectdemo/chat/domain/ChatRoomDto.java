package lee.projectdemo.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomDto {

    private Long itemId;
    private String senderId;
    private String displayName;
    private String itemName;

    public ChatRoomDto() {
    }

    public ChatRoomDto(Long itemId, String displayName, String itemName) {
        this.itemId = itemId;
        this.displayName = displayName;
        this.itemName = itemName;
    }
}
