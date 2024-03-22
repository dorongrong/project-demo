package lee.projectdemo.exception.exhandler;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.file.AccessDeniedException;

@Slf4j
//@ControllerAdvice
public class AuthenticationExceptionAdvice {

//    @ExceptionHandler(UserIdExistsException.class)
//    public void userIdExHandler(UserIdExistsException e) {
//
//        log.error("[userIdExistsException] ex TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTEST", e);
//    }

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

//    @ExceptionHandler(AuthenticationException.class)
//    public RedirectView AuthenticationException(AuthenticationException e) {
//        log.error("테에스트aaaa");
//        System.out.println("AAAAAAAA");
//        String redirectUrl = "/login";
//        return new RedirectView(redirectUrl);
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public RedirectView AccessDeniedException(AccessDeniedException e) {
        log.error("접근 불가");
        System.out.println("접근불가");
        String redirectUrl = "/logout";
        return new RedirectView(redirectUrl);
    }

//    @ExceptionHandler(SignatureException.class)
//    public RedirectView handleSignatureException() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 유효하지 않습니다."));
//    }
//
//    @ExceptionHandler(MalformedJwtException.class)
//    public ResponseEntity<ApiResponse> handleMalformedJwtException() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("올바르지 않은 토큰입니다."));
//    }

    @ExceptionHandler(ExpiredJwtException.class)
    public RedirectView handleExpiredJwtException(ExpiredJwtException e) {
        log.error("십라란ㅇ라", e);
        String redirectUrl = "/logout";
        return new RedirectView(redirectUrl);
    }


}
