package lee.projectdemo.login.config;

import lee.projectdemo.login.repository.MemoryUserRepository;
import lee.projectdemo.login.repository.UserRepository;
import org.springframework.context.annotation.Bean;

public class MemoryConfig {
    
    @Bean
    public UserRepository userRepository(){
        return new MemoryUserRepository();
    }

}
