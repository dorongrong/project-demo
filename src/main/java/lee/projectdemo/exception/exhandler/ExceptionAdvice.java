package lee.projectdemo.exception.exhandler;


import lee.projectdemo.exception.UserIdExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "lee.projectdemo")
public class ExceptionAdvice {

    @ExceptionHandler(UserIdExistsException.class)
    public void userIdExHandler(UserIdExistsException e) {
        log.error("[userIdExistsException] ex", e);
    }

}
