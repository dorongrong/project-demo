package lee.projectdemo.chat.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatDto {

    //진짜 보낸사람의 long ID
    private String sendUserId;
    //진짜 보낸 사람의 login Id
    private String sendUserLoginId;

    private Long readCount = 1L;

    private String message;

    private Long chatRoomId;

    private ChatUserState chatUserState;


    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;

    public ChatDto() {
    }
}
