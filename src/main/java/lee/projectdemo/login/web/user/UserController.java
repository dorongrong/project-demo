package lee.projectdemo.login.web.user;


import jakarta.validation.Valid;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.Address;
import lee.projectdemo.login.user.User;
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

    @GetMapping("/add")
    public String addForm(@ModelAttribute("user") User user) {

        return "user/addUserForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/addUserForm";
        }
        
        //이거 DTO객체를 만들어서 넣자 Address객체 수정 요함
        Address address = new Address(user.getAddress().getZipcode(),
                user.getAddress().getStreetAdr(), user.getAddress().getDetailAdr());

        User regisUser = new User(user.getLoginId(), user.getLoginName(), user.getPassword(), address);

        userRepository.save(regisUser);
        return "redirect:/";
    }

}
