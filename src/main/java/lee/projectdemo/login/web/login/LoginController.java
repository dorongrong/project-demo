package lee.projectdemo.login.web.login;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.login.service.LoginService;
import lee.projectdemo.login.user.SignResponse;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;


@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

//    @GetMapping("/login")
//    public String loginForm(@RequestParam(value = "error", required = false) String error,
//                            @ModelAttribute("loginForm") LoginForm form,
//                            BindingResult bindingResult ) {
//        // 영 마음에 안듬
//        if (error == null){
//            return "login/loginForm";
//        }
//        if (error.equals("wrong")) {
//            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//        }
//        if (error.equals("empty")) {
//            bindingResult.reject("loginFail", "아이디 또는 비밀번호를 잘못 입력했습니다.");
//        }
//        return "login/loginForm";
//    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String username,
                            @RequestParam(required = false) String error,
                            @RequestParam(required = false) String errorMessage,
                            @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult) {


        //empty는 글로벌 unknown도 글로벌 자격증명은 필드
        if ("empty".equals(error)) {
            bindingResult.reject("global", errorMessage);
        } else if ("badcredentials".equals(error)) {
            form.setLoginId(username);

            bindingResult.rejectValue("loginId", "Illegal");
            bindingResult.rejectValue("password", "Illegal");
            bindingResult.reject("loginId", errorMessage);
        } else if ("unknown".equals(error)) {
            bindingResult.reject("global", errorMessage);
        }

        return "login/loginForm";
    }


    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult
            bindingResult, HttpServletRequest request, HttpServletResponse response) {
//        if (bindingResult.hasErrors()) {
//            return "login/loginForm";
//        }
//
//        if (request.getAttribute("exception") == "test") {
//            bindingResult.reject("loginFail", "아이디가 맞지 않습니다.");
//            return "login/loginForm";
//        }
//
//        try {
//            SignResponse signResponse = loginService.login(form.getLoginId(), form.getPassword());
//
//            //이건 헤더에 추가
//            response.addHeader("Authorization", "Bearer " + signResponse.getToken());
//
//            // 쿠키 추가 밑에 3줄은 쿠키에 넣을려고 넣는거임
//            Cookie cookie = new Cookie("Authorization", signResponse.getToken());
//            cookie.setMaxAge(360000);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//
//            return "redirect:/";
//
//        } catch (NoSuchElementException e) {
//            bindingResult.reject("loginFail", "아이디가 맞지 않습니다.");
//            return "login/loginForm";
//
//        } catch (BadCredentialsException e) {
//            bindingResult.reject("loginFail", "비밀번호가 맞지 않습니다.");
//            return "login/loginForm";
//        }
        return "login/loginForm";
    }

    //이거 필터에도 있음
    private String getCookie(HttpServletRequest request){
        Cookie[] tokenCookie = request.getCookies();
        if (tokenCookie != null) {
            for (Cookie c : tokenCookie) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("Authorization")) {
                    return value;
                }
            }
        }
        return null;
    }
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
//        return "redirect:/";
//    }

//    @PostMapping("/logout")
//    public String logout(HttpServletRequest request) {
//        //세션을 삭제한다.
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//        return "redirect:/";
//    }
