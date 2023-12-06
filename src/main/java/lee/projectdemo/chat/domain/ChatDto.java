package lee.projectdemo.chat.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

//    private Long id;
//    private ChatRoom chatRoom;
//    private User user;
//
//    private String message;

    private Long chatRoomId;
    private String sendUserId;
    //Default unread
    private ChatState chatState;
    private String message;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;

}
