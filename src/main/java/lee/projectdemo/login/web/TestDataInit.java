package lee.projectdemo.login.web;

import jakarta.annotation.PostConstruct;
import lee.projectdemo.login.user.User;
import lee.projectdemo.login.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final UserRepository userRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {

        User user = new User();
        user.setLoginId("test");
        user.setPassword("test!");
        user.setLoginName("이성훈");

        userRepository.save(user);

    }

}