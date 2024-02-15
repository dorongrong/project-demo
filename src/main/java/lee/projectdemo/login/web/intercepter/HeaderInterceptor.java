package lee.projectdemo.login.web.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.login.service.LoginService;
import lee.projectdemo.login.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class HeaderInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String cToken = loginService.getCookie(request);
        Authentication user = loginService.getUserDetail(cToken);
        PrincipalDetails userDetails = (PrincipalDetails) user.getPrincipal();
        //토큰에서 빼온 유저 정보
        User tokenUser = userDetails.getUser();
        // 모든 요청의 모델에 사용자 정보 추가
        model.addAttribute("interceptorId", tokenUser.getId()); // 모델에 사용자 정보를 추가하여 Thymeleaf에서 사용할 수 있도록 함

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
