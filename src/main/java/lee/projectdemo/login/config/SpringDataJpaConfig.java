package lee.projectdemo.login.config;


import lee.projectdemo.login.repository.JpaUserRepository;
import lee.projectdemo.login.repository.SpringDataJpaUserRepository;
import lee.projectdemo.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {

    private final SpringDataJpaUserRepository springDataJpaUserRepository;

//    @Bean
//    public LoginService loginService() {
//        return new LoginService(userRepository());
//    }
    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository(springDataJpaUserRepository);
    }

}
