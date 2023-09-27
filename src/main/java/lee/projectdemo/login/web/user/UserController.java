package lee.projectdemo.login.web.user;


import jakarta.validation.Valid;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.service.LoginService;
import lee.projectdemo.login.user.Address;
import lee.projectdemo.login.user.User;
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

    private final UserRepository userRepository;
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

        if (loginService.isLoginIdExists(userDto.getLoginId()) == false){
            bindingResult.reject("loginIdExists", "동일한 아이디가 존재합니다.");
            return "user/addUserForm";
        }
        
        //이거 DTO객체를 만들어서 넣자 Address객체 수정 요함
        Address address = new Address(userDto.getAddressDto().getZipcode(),
                userDto.getAddressDto().getStreetAdr(), userDto.getAddressDto().getDetailAdr());

        User regisUser = new User(userDto.getLoginId(), userDto.getLoginName(), userDto.getPassword(), address);

        userRepository.save(regisUser);
        return "redirect:/";
    }

}
