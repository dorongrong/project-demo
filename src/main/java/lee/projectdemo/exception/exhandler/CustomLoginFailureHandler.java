package lee.projectdemo.exception.exhandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 실패 이유에 따라 적절한 failureUrl을 설정
        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            setDefaultFailureUrl("/login?error=wrong");
        } else if (exception instanceof InternalAuthenticationServiceException) {
            setDefaultFailureUrl("/login?error=empty");
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
            errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
            setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);
        }

//        if (exception.getMessage() == "") {
//            setDefaultFailureUrl("/login?error=empty");
//        }
//        if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
//            setDefaultFailureUrl("/login?error=notfound");
//        }
        super.onAuthenticationFailure(request, response, exception);
    }
}