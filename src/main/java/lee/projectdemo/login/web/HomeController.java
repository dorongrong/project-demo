package lee.projectdemo.login.web;


import jakarta.servlet.http.HttpServletRequest;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.chat.service.ChatRoomService;
import lee.projectdemo.item.aws.AwsS3Service;
import lee.projectdemo.item.item.ItemDto;
import lee.projectdemo.item.item.ItemSearchCond;
import lee.projectdemo.item.service.ItemService;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    //쿠키때문에
    private final LoginService loginService;

    private final ItemService itemService;

    private final AwsS3Service s3Service;

    private final ChatRoomService chatRoomService;

//    @GetMapping("/")
//    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
//                       User loginMember,
//                       Model model) {
//        //세션에 회원 데이터가 없으면 home
//        if (loginMember == null) {
//            return "home";
//        }
//        //세션이 유지되면 로그인으로 이동
//        model.addAttribute("user", loginMember);
//        return "loginHome";
//    }

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model,
                       @PageableDefault(page = 0, size = 9, sort = "item_id", direction = Sort.Direction.DESC) Pageable pageable) {
        //세션에 회원 데이터가 없으면 home
        System.out.println("홈으로 리다이렉트으");

        // 최신 등록 아이템만 보여주기 위해 빈 ItemSearchCond 값 생성
        ItemSearchCond cond = new ItemSearchCond(null, null);
        Page<ItemDto> itemList = s3Service.addImageItemDto(itemService.findAllItemPage(cond, pageable), pageable);

        model.addAttribute("itemList", itemList);

        if (loginService.getCookie(request) == null) {
            return "home";
        }
//        String cToken = loginService.getCookie(request);
        String cToken = loginService.getCookie(request);

        Authentication user = loginService.getUserDetail(cToken);

            if (user != null) {
                PrincipalDetails userDetails = (PrincipalDetails)user.getPrincipal();
                String username = userDetails.getUsername();
                String userId = userDetails.getLoginId();
                model.addAttribute("user", username);
                model.addAttribute("id", userId);
                //다시 변경

                //로그인한 순간 유저의 아이템 전부 구독 X 본인의 아이템 큐를 동적 생성하는거임
//                List<Item> items = userDetails.getItem();
//                if (!items.isEmpty())
//                chatRoomService.subscribeQueue(items,userId);
                return "home";
            }

        return "home";

    }

    @GetMapping("/api/hello")
    public ResponseEntity<List<String>> getHello() {
        List<String> messages = Arrays.asList("리액트 스프링", "연결 성공");
        return ResponseEntity.ok(messages);
    }



}