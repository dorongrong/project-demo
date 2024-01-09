package lee.projectdemo.chat.web;


import lee.projectdemo.chat.domain.ChatRoomDto;
import lee.projectdemo.chat.domain.ChatUserState;
import lee.projectdemo.chat.domain.ChatUserStateDto;
import lee.projectdemo.chat.service.ChatRoomService;
import lee.projectdemo.chat.service.UserStateService;
import lee.projectdemo.item.item.ItemFetchDto;
import lee.projectdemo.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;

    private final UserStateService userStateService;

    private final ItemService itemService;

    @PostMapping("/api/chat")
    public ResponseEntity<ConcurrentHashMap<String, Object>> chatEnterFetch(@RequestBody ChatRoomDto chatRoomDto) {
        //senderId와 itemId를 사용해서 채팅방의 유무를 확인후 채팅방이 있으면 가져오고
        //없으면 생성하는데 senderId가 itemId의 판매자일경우 생성을 막음

        System.out.println("fetch api 시작");
        ChatRoomDto saveChatRoom = chatRoomService.getOrSaveChatRoom(chatRoomDto);

        ItemFetchDto item = itemService.getFetchItem(chatRoomDto.getItemId());

        Long otherUser = saveChatRoom.getOtherUserId();
        //온라인으로 전환
        userStateService.setUserState(chatRoomDto.getSendUserId(), ChatUserState.ONLINE);
        //채팅 상대방 상태 획득
        ChatUserStateDto userStateDto = userStateService.getUserState(otherUser);
        System.out.println("상대방 아이디" + otherUser);
        //채팅내역 보내주기 전에 채팅방을 가져와서 상대방이 보낸 모든 채팅 charRead값을 2로 바꾼 후 전송
        // 여러 객체를 Map에 담아서 반환
        ConcurrentHashMap<String, Object> responseMap = new ConcurrentHashMap<>();
        responseMap.put("userStateDto", userStateDto);
        responseMap.put("ChatRoom", saveChatRoom);
        responseMap.put("Item", item);

        System.out.println("맵 확인@@1" + responseMap);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("/api/unmount")
    public ResponseEntity<String> chatExitFetch(@RequestBody ChatRoomDto chatRoomDto) {
        System.out.println("fetch api 언마운트!! 시작");

        userStateService.setUserState(chatRoomDto.getSendUserId(), ChatUserState.OFFLINE);

        return new ResponseEntity<>("언마운트시작", HttpStatus.OK);
    }


}
