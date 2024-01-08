package lee.projectdemo.chat.domain;

import lombok.Data;

@Data
public class ChatUserStateDto {

    private Long userId;
    private ChatUserState userState;

    // 생성자, Getter, Setter 생략

    // 생성자
    public ChatUserStateDto() {
    }

    public ChatUserStateDto(Long userId, ChatUserState userState) {
        this.userId = userId;
        this.userState = userState;
    }
}
