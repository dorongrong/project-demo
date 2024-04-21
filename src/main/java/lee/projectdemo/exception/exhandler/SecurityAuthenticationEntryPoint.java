package lee.projectdemo.exception.exhandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String LOGOUT_URL = "/logout";
    private static final String LOGIN_URL = "/login";

//    private final HandlerExceptionResolver resolver;
//
//    public SecurityAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
//        this.resolver = resolver;
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

//        authException.printStackTrace();
        //예외 원인마다 다르게 처리하면됨
        if (authException.getCause() instanceof JwtException) {
            if(authException.getCause() instanceof ExpiredJwtException){
                response.sendRedirect(LOGOUT_URL);
            }
            if(authException.getCause() instanceof MalformedJwtException){
                response.sendRedirect(LOGOUT_URL);
            }
            if(authException.getCause() instanceof UnsupportedJwtException){
                response.sendRedirect(LOGOUT_URL);
            }
            if(authException.getCause() instanceof IllegalArgumentException){
                response.sendRedirect(LOGOUT_URL);
            }
        }
        else {
            //JWTException이 아닌경우 이미 ExceptionTranslationFilter에서 예외가 정제되어 나온걸 commence를 호출한거기에 따로 분류 X

            response.sendRedirect(LOGOUT_URL);
        }

    }
}
