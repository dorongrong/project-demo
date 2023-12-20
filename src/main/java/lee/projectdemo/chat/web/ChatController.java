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
        //senderId와 itemId를 사용해서 채팅방의 유무를 확인후 채팅방이 있으면 가져오고
        //없으면 생성하는데 senderId가 itemId의 판매자일경우 생성을 막음
        System.out.println("드가자");
        chatService.getOrSaveChatRoom(chatRoomDto);
        return new ResponseEntity<>("채팅방이 저장되었습니다.", HttpStatus.OK);
    }


}
