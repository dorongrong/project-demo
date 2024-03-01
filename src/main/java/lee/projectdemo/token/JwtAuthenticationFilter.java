package lee.projectdemo.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        // 헤더에서 JWT 를 받아옵니다.
//        String token = jwtProvider.resolveToken(request);
//        // 유효한 토큰인지 확인합니다.
//        if (token != null && jwtProvider.validateToken(token)) {
//            // check access token
//            token = token.split(" ")[1].trim();
//            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
//            Authentication auth = jwtProvider.getAuthentication(token);
//            // SecurityContext 에 Authentication 객체를 저장합니다.
//            // 토큰은 Context가 필요 없는거 아닌가? -> 아니다 추후 다른 필터에서 작업을 위해 필요해서 넣어줘야한다.
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        filterChain.doFilter(request, response);
//    }

    //쿠키용 필턴
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠기에 들어가있는 value가 담김
        String cToken = "Bearer " + getCookie(request);

        //음....
        try{
            if (jwtProvider.validateToken(cToken)) {
                cToken = cToken.split(" ")[1].trim();

                Authentication auth = jwtProvider.getAuthentication(cToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);

        } catch(ExpiredJwtException | MalformedJwtException e) {
//            throw new AccessDeniedException("테스트");
            throw new BadCredentialsException("테스트", e);
//            response.sendRedirect("/login");
        }


//        Authentication auth = jwtProvider.getAuthentication(cToken);
//        SecurityContextHolder.getContext().setAuthentication(auth);

//        filterChain.doFilter(request, response);

    }

    private String getCookie(HttpServletRequest request){
        Cookie[] tokenCookie = request.getCookies();
        if (tokenCookie != null) {
            for (Cookie c : tokenCookie) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("Authorization")) {
                    return value;
                }
            }
        }
        return null;
    }

}
