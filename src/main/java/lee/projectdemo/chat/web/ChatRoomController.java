package lee.projectdemo.chat.web;

import jakarta.servlet.http.HttpServletRequest;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.chat.domain.ChatRoom;
import lee.projectdemo.login.service.LoginService;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    //    이 페이지는 개인의 채팅목록을 만드는거라 url이 같다고 해서 보여주면 안됨

    private final LoginService loginService;

    @GetMapping("/chat")
    public String items(Model model, HttpServletRequest request) {

        String cToken = loginService.getCookie(request);
        Authentication user = loginService.getUserDetail(cToken);
        PrincipalDetails userDetails = (PrincipalDetails)user.getPrincipal();
        //토큰에서 빼온 유저 정보
        User tokenUser = userDetails.getUser();
        List<ChatRoom> sellRoom = tokenUser.getSellingChatRooms();
        List<ChatRoom> buyRoom = tokenUser.getBuyingChatRooms();

        List<ChatRoom> allRoom = new ArrayList<>();
        allRoom.addAll(sellRoom);
        allRoom.addAll(buyRoom);

        List<String> chatRoomName = new ArrayList<>();

        for (ChatRoom chatRoom : allRoom) {
            if (chatRoom.getSeller().equals(tokenUser)) {
                // If the seller is the same as tokenUser, add buyer's loginId to the list
                chatRoomName.add(chatRoom.getBuyer().getLoginId());
            } else {
                // If the seller is different from tokenUser, add tokenUser's loginId to the list
                chatRoomName.add(tokenUser.getLoginId());
            }
        }

        model.addAttribute("chatRoom", chatRoomName);

        return "chat/myChatRoom";
    }


}
