package lee.projectdemo.login.web.login;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lee.projectdemo.auth.PrincipalDetailsService;
import lee.projectdemo.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final PrincipalDetailsService userDetailsService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @ModelAttribute("loginForm") LoginForm form,
                            BindingResult bindingResult ) {
        // 영 마음에 안듬
        if (error == null){
            return "login/loginForm";
        }
        if (error.equals("notfound")) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
        }
        if (error.equals("empty")) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호를 입력해주세요.");
        }
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult
            bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(form.getLoginId());
        } catch (UsernameNotFoundException e) {
            //로그찍어
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "redirect:/";
        }


//        User loginUser = loginService.login(form.getLoginId(),
//                form.getPassword());
//        log.info("login? {}", loginUser);
//
//        if (loginUser == null) {
//            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//            return "login/loginForm";
//        }

//        //로그인 성공 처리
//        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
//        HttpSession session = request.getSession();
//        //세션에 로그인 회원 정보 보관
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginUser);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}