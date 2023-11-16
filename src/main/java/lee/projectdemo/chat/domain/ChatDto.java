package lee.projectdemo.chat.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lee.projectdemo.login.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatDto {

    private Long id;
    private ChatRoom chatRoom;
    private User user;

    private String message;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;

}
