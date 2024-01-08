package lee.projectdemo.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    private Long sendUserId;

    private Long readCount = 1L;

    private String message;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore //@toString을 설정을 해도 response로 chat 을 보내면 json직렬화 과정에서 무한루프가 뜬다 해당 애노테이션이 해결해주는데 
    //한번 알아보자
    private ChatRoom chatRoom;

    private LocalDateTime regDate;
    @PrePersist
    protected void onCreate() {
        regDate = LocalDateTime.now();
    }

    public Chat() {
    }

    public Chat(Long sendUserId, Long readCount, String message, ChatRoom chatRoom) {
        this.sendUserId = sendUserId;
        this.readCount = readCount;
        this.message = message;
        this.chatRoom = chatRoom;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                ", sendUserId=" + sendUserId +
                ", readCount=" + readCount +
                ", message='" + message + '\'' +
                ", regDate=" + regDate +
                '}';
    }
}
