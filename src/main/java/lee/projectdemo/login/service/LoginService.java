package lee.projectdemo.login.service;


import lee.projectdemo.login.user.User;
import lee.projectdemo.login.user.UserRepository;
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

}
