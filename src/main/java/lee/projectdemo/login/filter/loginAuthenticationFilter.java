package lee.projectdemo.login.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.exception.UserIdOrPasswordExistsException;
import lee.projectdemo.login.user.User;
import lee.projectdemo.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;


@RequiredArgsConstructor
public class loginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");

        if (loginId == null || loginId.isEmpty() || password == null || password.isEmpty()){
            throw new UserIdOrPasswordExistsException("아이디 혹은 비밀번호를 입력해주세요.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        Authentication authentication = getAuthenticationManager().authenticate(authenticationToken);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        User user = principalDetails.getUser();

        String token = jwtProvider.createToken(user.getLoginId(), user.getUserRole(), user.itemListId(), user.getId());

        //이건 헤더에 추가
        response.addHeader("Authorization", "Bearer " + token);

        // 쿠키 추가 밑에 3줄은 쿠키에 넣을려고 넣는거임
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setMaxAge(360000);
        cookie.setPath("/");
        response.addCookie(cookie);

        super.successfulAuthentication(request, response, chain, authResult);
    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        System.out.println("로그인 실패");
//        request.setAttribute("exception", "test");
//        response.sendRedirect("/login");
////        super.unsuccessfulAuthentication(request, response, failed);
//    }


}



