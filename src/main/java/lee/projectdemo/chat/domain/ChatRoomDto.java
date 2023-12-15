package lee.projectdemo.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomDto {

    private Long itemId;
    private String senderId;

    public ChatRoomDto() {
    }


}
