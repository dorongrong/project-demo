package lee.projectdemo.login.web;


import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails userData, Model model) {
        //세션이 유지되면 로그인으로 이동
        if (userData != null) {
            model.addAttribute("user", userData.getUsername());
            return "loginHome";
        }

        //세션에 회원 데이터가 없으면 home
        return "home";
    }
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

//    @GetMapping("/")
//    public String home() {
//        //세션에 회원 데이터가 없으면 home
//
//        return "loginHome";
//    }

}