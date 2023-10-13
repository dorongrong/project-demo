package lee.projectdemo.exception.exhandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 실패 이유에 따라 적절한 failureUrl을 설정
        System.out.println(exception.getMessage());
        if (exception instanceof UsernameNotFoundException) {
            String errorMessage = exception.getMessage();
            System.out.println(errorMessage + "증명");
            if ("아이디를 입력하세요.".equals(errorMessage)) {
                // 아이디를 입력하지 않았을 때의 처리
                setDefaultFailureUrl("/login?error=empty");
            } else {
                // 존재하지 않는 아이디일 때의 처리
                setDefaultFailureUrl("/login?error=notfound");
            }
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