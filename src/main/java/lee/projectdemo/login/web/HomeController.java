package lee.projectdemo.login.web;


import jakarta.servlet.http.HttpServletRequest;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

//    @GetMapping("/")
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

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        //세션에 회원 데이터가 없으면 home
        System.out.println("홈으로 리다이렉트으");

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
                return "loginHome";
            }

        return "home";

    }

}