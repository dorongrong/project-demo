package lee.projectdemo.exception.exhandler;


import lee.projectdemo.exception.NoValue;
import lee.projectdemo.exception.UserIdExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {


    @ExceptionHandler(UserIdExistsException.class)
    public void userIdExHandler(UserIdExistsException e) {

        log.error("[userIdExistsException] ex TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTEST", e);
    }

    @ExceptionHandler(NoValue.class)
    public RedirectView noValueException(NoValue e) {
        log.error("아이디를 입력하세요. ++", e);
        String redirectUrl = "/login?error=empty";
        return new RedirectView(redirectUrl);
    }

//    @ExceptionHandler(UserIdExistsException.class)
//    public ModelAndView userIdExHandler(UserIdExistsException e, BindingResult bindingResult) {
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("signUpIdEx", "아이디가 중복되었습니다.");
//        modelAndView.setViewName("user/addUserForm");
//
//        log.error("[userIdExistsException] ex TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTEST", e);
//        return modelAndView;
//    }

}
