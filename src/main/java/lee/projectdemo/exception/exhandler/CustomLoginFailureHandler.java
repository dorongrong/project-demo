package lee.projectdemo.exception.exhandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lee.projectdemo.exception.UserIdMismatchException;
import lee.projectdemo.exception.UserIdOrPasswordExistsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // 실패 이유에 따라 적절한 failureUrl을 설정
        String username = "";
        String error;
        String errorMessage;

        if (exception instanceof UserIdOrPasswordExistsException) {
            error = "empty";
            errorMessage = exception.getMessage();
        } else if (exception instanceof BadCredentialsException) {
            username = request.getParameter("loginId");
            error = "badcredentials";
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            error = "unknown";
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
//            errorMessage = exception.getMessage();
        } else {
            error = "unknown";
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
        }

        username = URLEncoder.encode(username, "UTF-8");
        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/login?username=" + username + "&error=" + error + "&errorMessage=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}