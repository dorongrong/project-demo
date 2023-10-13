package lee.projectdemo;

import lee.projectdemo.exception.exhandler.CustomLoginFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행합니다.
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어줍니다. (Security ContextHolder 라는 키값을 가진)
// 이 세션에 저장되어있는 오브젝트는 Authentication 이라는 타입의 객체만 들어갈수있음
// 당연히 Authentication 안에 User 정보가 있어야 됨. 이 User의 정보는 UserDetails 라는 타입의 객체임

// Security Session => Authentication => UserDetails

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomLoginFailureHandler customLoginFailureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/").permitAll() //  /** 주소로 다 들어갈수있음
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/users/add").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // /admin/** 주소로는 ADMIN role을 가진사람만 가능
                        .anyRequest().authenticated() // 다른 주소는 모두 로그인 필요!
                )
                .formLogin((form) -> form
                        .loginPage("/login") // 들어가지 못하는 주소로 들어갔을때 /login으로 가짐
                        .failureHandler(customLoginFailureHandler) // 커스텀 로그인 실패 핸들러 등록
                        .loginProcessingUrl("/login") // 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행 한마디로 로그인 안만들어도됨 ㅋㅋ
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/") //로그인이 완료될시 주소로 이동
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

}