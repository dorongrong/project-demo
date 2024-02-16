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
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class HeaderInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

        if (modelAndView != null) {

            String cToken = loginService.getCookie(request);
            if(cToken != null) {
            Authentication user = loginService.getUserDetail(cToken);
            PrincipalDetails userDetails = (PrincipalDetails) user.getPrincipal();
            //토큰에서 빼온 유저 정보
            User tokenUser = userDetails.getUser();

            modelAndView.getModel().put("tokenId", tokenUser.getId());
            }
        }

    }
}
