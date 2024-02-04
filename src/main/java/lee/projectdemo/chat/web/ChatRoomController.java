package lee.projectdemo.chat.web;

import jakarta.servlet.http.HttpServletRequest;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.chat.domain.ChatRoom;
import lee.projectdemo.chat.domain.ChatRoomDto;
import lee.projectdemo.item.aws.AwsS3Service;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.service.ItemService;
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
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    //    이 페이지는 개인의 채팅목록을 만드는거라 url이 같다고 해서 보여주면 안됨

    private final LoginService loginService;

    private final ItemService itemService;

    private final AwsS3Service s3Service;

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


        List<ChatRoomDto> chatRoomDto = new ArrayList<>();

        for (ChatRoom chatRoom : allRoom) {
            //이미지 추가
            Item item = itemService.getItem(chatRoom.getItem().getId()).get();

            // 이미지 URL을 얻기 위한 메서드 호출
            List<String> imageURLs = getImageURLs(item);

            // 삼항 연산자를 사용하여 판매자 비교
            String loginId = chatRoom.getSeller().equals(tokenUser) ? chatRoom.getBuyer().getLoginId() : chatRoom.getSeller().getLoginId();

            chatRoomDto.add(new ChatRoomDto(chatRoom.getItem().getId(), loginId,
                    chatRoom.getItem().getItemName(), chatRoom.getBuyer().getId(), imageURLs));

            //판매자랑 사용자랑 같은 사람일때
//            if (chatRoom.getSeller().equals(tokenUser)) {
//
//                List<String> imageURLs = new ArrayList<>();
//                if(item.getImages().size() == 0){
//                    //이미지가 없을 경우 대체이미지 삽입
//                    imageURLs.add(s3Service.loadEmptyImage());
//                }
//                else{
//                    for (Image image : item.getImages()) {
//                        imageURLs.add(s3Service.loadImage(image.getStoreFileName()));
//                    }
//                }
//                //이미지 ^
//                chatRoomDto.add(new ChatRoomDto(chatRoom.getItem().getId(), chatRoom.getBuyer().getLoginId(),
//                        chatRoom.getItem().getItemName(), chatRoom.getBuyer().getId(), imageURLs));
//            } else {
//                //이미지 추가
//                List<String> imageURLs = new ArrayList<>();
//                if(item.getImages().size() == 0){
//                    //이미지가 없을 경우 대체이미지 삽입
//                    imageURLs.add(s3Service.loadEmptyImage());
//                }
//                else{
//                    for (Image image : item.getImages()) {
//                        imageURLs.add(s3Service.loadImage(image.getStoreFileName()));
//                    }
//                }
//                //이미지
//
//                chatRoomDto.add(new ChatRoomDto(chatRoom.getItem().getId(), chatRoom.getSeller().getLoginId(),
//                        chatRoom.getItem().getItemName(), chatRoom.getBuyer().getId(), imageURLs));
//            }

        }

        model.addAttribute("chatRoom", chatRoomDto);

        return "chat/myChatRoom";
    }

        // 이미지 URL을 얻기 위한 메서드
        private List<String> getImageURLs(Item item) {
            return item.getImages().stream()
                    .map(image -> s3Service.loadImage(image.getStoreFileName()))
                    .collect(Collectors.toList());
        }


}
