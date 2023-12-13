package lee.projectdemo.chat.web;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @PostMapping("/api/createChat")
    public ResponseEntity<String> test(
//            @RequestParam String itemId,
//            @RequestParam String senderId
    ) {
        System.out.println("출력ddddddddddddddddddd");
//        System.out.println("출력" + itemId + senderId);
        return new ResponseEntity<>("채팅방이 저장되었습니다.", HttpStatus.OK);
    }

}
