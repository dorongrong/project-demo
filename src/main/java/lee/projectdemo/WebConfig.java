package lee.projectdemo;

import lee.projectdemo.login.web.intercepter.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1) //첫번째로 체크
                .addPathPatterns("/**") //모든 경로 체크
                .excludePathPatterns(
                        "/", "/users/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error"
                ); //예외 추가 (ex : "/"은 홈페이지때문에 세션이 없어도 갈수있어야함)
    }

}
