package lee.projectdemo.exception.exhandler;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class AuthenticationExceptionAdvice {

//    @ExceptionHandler(NoValue.class)
//    public RedirectView noValueException(NoValue e) {
//        log.error("아이디를 입력하세요. ++", e);
//        String redirectUrl = "/login?error=empty";
//        return new RedirectView(redirectUrl);
//    }

//    @ExceptionHandler(Exception.class)
//    public RedirectView testException(Exception e) {
//        log.error("테에스트ddd");
//        System.out.println("xxxxxxxxx");
//        return new RedirectView("/logout");
//    }

    @ExceptionHandler(AuthenticationException.class)
    public RedirectView AuthenticationException(AuthenticationException e) {
        String redirectUrl = "/logout";
        return new RedirectView(redirectUrl);
    }

//    @ExceptionHandler(SignatureException.class)
//    public RedirectView handleSignatureException() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 유효하지 않습니다."));
//    }
//
    @ExceptionHandler(MalformedJwtException.class)
    public RedirectView handleMalformedJwtException(ExpiredJwtException e) {
        String redirectUrl = "/logout";
        return new RedirectView(redirectUrl);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public RedirectView handleExpiredJwtException(ExpiredJwtException e) {
        System.out.println("테스트 토큰");
        String redirectUrl = "/logout";
        return new RedirectView(redirectUrl);
    }


}
