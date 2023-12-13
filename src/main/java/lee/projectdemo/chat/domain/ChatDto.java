package lee.projectdemo.chat.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatDto {

    private Long id;

    private String sendUserId;

    private Long readCount = 1L;

    private String message;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;

    public ChatDto() {
    }
}
