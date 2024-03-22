package lee.projectdemo.test;


import lee.projectdemo.exception.UserIdOrPasswordExistsException;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.service.LoginService;
import lee.projectdemo.login.user.AddressDto;
import lee.projectdemo.login.user.UserDto;
import lee.projectdemo.login.web.user.UserController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class ExceptionTest {
    
    @Autowired UserRepository userRepository;
    @Autowired LoginService loginService;
    @Autowired UserController userController;

    @Test
    void UserIdExists() {

        assertThatThrownBy(() -> loginService.signUp(new UserDto("donix", "이지훈", "l4319634",
                new AddressDto("1234", "4567", "103동 203호"))))
                .isInstanceOf(UserIdOrPasswordExistsException.class);
    }

}
