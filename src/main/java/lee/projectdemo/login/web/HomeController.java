package lee.projectdemo.login.web;


import jakarta.servlet.http.HttpServletRequest;
import lee.projectdemo.auth.PrincipalDetails;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    //쿠키때문에
    private final LoginService loginService;

    private final ItemService itemService;

    private final AwsS3Service s3Service;

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
        Page<ItemDto> itemList = s3Service.addImageItemDto(itemService.findAllItemPage(cond , pageable), pageable);

        model.addAttribute("itemList", itemList);

        if (loginService.getCookie(request) == null){
            return "home";
        }
//        String cToken = loginService.getCookie(request);
        String cToken = loginService.getCookie(request);

        Authentication user = loginService.getUserDetail(cToken);

            if (user != null) {
                PrincipalDetails userDetails = (PrincipalDetails)user.getPrincipal();
                String username = userDetails.getUsername();
                model.addAttribute("user", username);
                //다시 변경
                return "home";
            }

        return "home";

    }



}