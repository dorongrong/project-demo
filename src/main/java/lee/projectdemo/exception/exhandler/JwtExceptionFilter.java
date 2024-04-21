package lee.projectdemo.exception.exhandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    //    private final HandlerExceptionResolver resolver;
    public JwtExceptionFilter(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

//    public JwtExceptionFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
//        this.resolver = resolver;
//    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch(JwtException e){

            AuthenticationException authException = new BadCredentialsException(e.getMessage(), e);
            authenticationEntryPoint.commence(request, response, authException);
//            response.sendRedirect("/logout");
//            resolver.resolveException(request, response, null, e);
        }
    }


}
