package lee.projectdemo;

import lee.projectdemo.auth.PrincipalDetailsService;
import lee.projectdemo.exception.exhandler.CustomLoginFailureHandler;
import lee.projectdemo.exception.exhandler.SecurityAuthenticationEntryPoint;
import lee.projectdemo.login.filter.loginAuthenticationFilter;
import lee.projectdemo.token.JwtAuthenticationFilter;
import lee.projectdemo.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final JwtProvider jwtProvider;

    private final UserDetailsService principalDetailsService;

    private final CustomLoginFailureHandler customLoginFailureHandler;

    private final AuthenticationEntryPoint securityAuthenticationEntryPoint;
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
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 세션 사용 안 함
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
                //test 이부분 authenticationManager의 userDetails등 필요한것들을 설정하는 방법 분기가 갈림
                .addFilter(loginAuthenticationFilter())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/*").permitAll()
                        .requestMatchers("/").permitAll() //  /** 주소로 다 들어갈수있음
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/users/add").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/static/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // /admin/** 주소로는 ADMIN role을 가진사람만 가능)
                        .anyRequest().authenticated() // 다른 주소는 모두 로그인 필요!
                )
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), jwtProvider),
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
                        //인증 예외시 커스텀 JwtAuthenticationEntryPoint 구현체 사용
                        .authenticationEntryPoint(securityAuthenticationEntryPoint));
//                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));

        return http.build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter loginAuthenticationFilter() {
        UsernamePasswordAuthenticationFilter filter = new loginAuthenticationFilter(jwtProvider);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationFailureHandler(customLoginFailureHandler);

        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(principalDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//            throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }



}