package lee.projectdemo.login.service;


import lee.projectdemo.exception.UserIdExistsException;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.User;
import lee.projectdemo.login.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public User login(String loginId, String password) {
        return userRepository.findByLoginId(loginId).filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    public String signUp(UserDto userDto) {
        if(signUpIdExists(userDto.getLoginId()) == false){
            throw new UserIdExistsException("이미 존재하는 아이디입니다.");
        }
        return "1";
    }

    private boolean signUpIdExists(String loginId) {
        return userRepository.findByLoginId(loginId).isEmpty();
    }

}
