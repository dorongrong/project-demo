package lee.projectdemo.chat.web;


import lee.projectdemo.chat.domain.ChatRoomDto;
import lee.projectdemo.chat.domain.ChatUserState;
import lee.projectdemo.chat.domain.ChatUserStateDto;
import lee.projectdemo.chat.service.ChatService;
import lee.projectdemo.chat.service.UserStateService;
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

    private final UserStateService userStateService;

    @PostMapping("/api/chat")
    public ResponseEntity<ChatUserStateDto> test(@RequestBody ChatRoomDto chatRoomDto) {
        //senderId와 itemId를 사용해서 채팅방의 유무를 확인후 채팅방이 있으면 가져오고
        //없으면 생성하는데 senderId가 itemId의 판매자일경우 생성을 막음

        System.out.println("fetch api 시작");
        ChatRoomDto saveChatRoom = chatService.getOrSaveChatRoom(chatRoomDto);
        Long otherUser = saveChatRoom.getOtherUserId();
        //온라인으로 전환
        userStateService.setUserStatus(chatRoomDto.getUserId(), ChatUserState.ONLINE);
        //채팅 상대방 상태 획득
        ChatUserStateDto userStatusDto = userStateService.getUserStatus(otherUser);
        System.out.println("상대방 아이디" + otherUser);

        return new ResponseEntity<>(userStatusDto, HttpStatus.OK);
    }

    @PostMapping("/api/chatunmount")
    public ResponseEntity<String> test2(@RequestBody ChatRoomDto chatRoomDto) {
        System.out.println("fetch api 언마운트!! 시작");

        userStateService.setUserStatus(chatRoomDto.getUserId(), ChatUserState.OFFLINE);

        return new ResponseEntity<>("언마운트시작", HttpStatus.OK);
    }


}
