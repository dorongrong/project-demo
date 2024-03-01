package lee.projectdemo;

import lee.projectdemo.login.web.intercepter.HeaderInterceptor;
import lee.projectdemo.login.web.intercepter.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    HandlerInterceptor headerInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(headerInterceptor)
                .order(1) //첫번째로 체크
                .addPathPatterns("/**") //모든 경로 체크
                .excludePathPatterns(
                        "/css/**", "/*.ico", "/error"
                );
//                .excludePathPatterns(
//                        "/", "/login", "/items", "/logout",
//                        "/css/**", "/*.ico", "/error"
//                ); //예외 추가 (ex : "/"은 홈페이지때문에 세션이 없어도 갈수있어야함)
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/{spring:\\w+}")
//                .setViewName("forward:/");
//        registry.addViewController("/**/{spring:\\w+}")
//                .setViewName("forward:/");
//        registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css)$}")
//                .setViewName("forward:/");
//    }


}
