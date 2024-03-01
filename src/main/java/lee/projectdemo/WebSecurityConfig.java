package lee.projectdemo;

import lee.projectdemo.exception.exhandler.CustomLoginFailureHandler;
import lee.projectdemo.token.JwtAuthenticationFilter;
import lee.projectdemo.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

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

    private final JwtProvider jwtProvider;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // sessionManagement는 세션 안쓰겠다는거임
        http
                // CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 사용 안 함
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 일반적인 루트가 아닌 다른 방식으로 요청시 거절, header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다.
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(c -> {
                            CorsConfigurationSource source = request -> { //react 가 있던데 확인하자
                                // Cors 허용 패턴
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOriginPatterns(
                                        List.of("*")
                                );
                                config.setAllowedMethods(
                                        List.of("*")
                                );
                                // 허용할 Header 설정
                                config.addAllowedHeader("*");
                                config.setAllowCredentials(true);
                                return config;
                            };
                            c.configurationSource(source);
                        }
                )
                .formLogin(formLogin -> formLogin.disable())
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/*", "/").permitAll() //  /** 주소로 다 들어갈수있음
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/users/add").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/static/js/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN") // /admin/** 주소로는 ADMIN role을 가진사람만 가능)
                    .anyRequest().authenticated() // 다른 주소는 모두 로그인 필요!
                    )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        // 로그아웃 핸들러 추가
//                        .addLogoutHandler((request, response, authentication) -> {
////                            // 사실 굳이 내가 세션 무효화하지 않아도 됨.
////                            // LogoutFilter가 내부적으로 해줌.
////                            HttpSession session = request.getSession();
////                            if (session != null) {
////                                session.invalidate();
////                            }
////                        })
                        // 로그아웃 성공 핸들러
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.sendRedirect("/login");
                        })
                        .deleteCookies("Authorization")) // 로그아웃 후 삭제할 쿠키 지정
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 로그인한 사용자만 한해서 권한 체크함
                        .accessDeniedPage("/login")
                        //로그인 하지 않은 사용자가 로그인시 리다이렉트
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));



//                .logout((logout) -> logout.permitAll());


//                .formLogin((form) -> form
//                        .loginPage("/login") // 들어가지 못하는 주소로 들어갔을때 /login으로 가짐
//                        .failureHandler(customLoginFailureHandler) // 커스텀 로그인 실패 핸들러 등록
//                        .loginProcessingUrl("/login") // 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행 한마디로 로그인 안만들어도됨 ㅋㅋ
//                        .usernameParameter("loginId")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/") //로그인이 완료될시 주소로 이동
//                        .permitAll()
//                )
//                .logout((logout) -> logout.permitAll());

        return http.build();
    }

}