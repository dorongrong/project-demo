package lee.projectdemo.chat.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatRoomId;

    private String sendUserId;

    private Long readCount = 1L;

    private String message;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    private LocalDateTime regDate;
    @PrePersist
    protected void onCreate() {
        regDate = LocalDateTime.now();
    }

    public Chat() {
    }

}
