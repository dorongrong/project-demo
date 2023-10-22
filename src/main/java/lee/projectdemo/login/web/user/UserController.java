package lee.projectdemo.login.web.user;


import jakarta.validation.Valid;
import lee.projectdemo.exception.UserIdExistsException;
import lee.projectdemo.login.service.LoginService;
import lee.projectdemo.login.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final LoginService loginService;


    @GetMapping("/add")
    public String addForm(@ModelAttribute("userDto") UserDto userDto) {
        return "user/addUserForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/addUserForm";
        }

//        if (loginService.signUpIdExists(userDto.getLoginId()) == false){
//            bindingResult.reject("loginIdExists", "동일한 아이디가 존재합니다.");
//            return "user/addUserForm";
//        }
        try {
            loginService.signUp(userDto);
            return "redirect:/";
        }
//        catch (UserIdExistsException e) {
//            throw e;
//        }
        catch (UserIdExistsException e) {
            bindingResult.reject("loginIdExists", "동일한 아이디가 존재합니다.");
            return "user/addUserForm";
        }
    }


//    @PostMapping("/add")
//    public ResponseEntity<JwtToken> loginSuccess(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
//        JwtToken token = loginService.login(userDto.getLoginId(), userDto.getPassword());
//        return ResponseEntity.ok(token);
//    }

}
