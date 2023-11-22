package lee.projectdemo.chat.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chat/rooms")
    public String getRooms(){
        return "chat/rooms";
    }

    @GetMapping(value = "/chat/room")
    public String getRoom(Long chatRoomId, String nickname, Model model){

        model.addAttribute("chatRoomId", chatRoomId);
        model.addAttribute("nickname", nickname);

        return "chat/room";
    }

}
