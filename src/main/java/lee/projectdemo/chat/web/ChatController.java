package lee.projectdemo.chat.web;


import lee.projectdemo.chat.domain.ChatRoomDto;
import lee.projectdemo.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/api/chat")
    public ResponseEntity<String> test(@RequestBody ChatRoomDto chatRoomDto) {
        chatService.chatRoomSave(chatRoomDto);
        return new ResponseEntity<>("채팅방이 저장되었습니다.", HttpStatus.OK);
    }


}
