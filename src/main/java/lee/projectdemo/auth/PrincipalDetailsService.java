package lee.projectdemo.auth;

import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
//시큐리티 설정에서 loginprocessingUrl("/login")
//요청이 오면 자동으로 UserDetailService 타입으로 IOC 되어 있는 loadUserByUsername 함수가 실행됨
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 생성된 security session 의 Auithentication 의 UserDetails에 들어감
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        try {
            User userEntity = userRepository.findByLoginId(loginId).get();
            return new PrincipalDetails(userEntity);
        }
        catch (NoSuchElementException e){
            // 로그 찍어라
            System.out.println("아이디 안적음");
            throw new UsernameNotFoundException("아이디 안적었다고");
        }
        catch (AuthenticationException e) {
            //로그
            System.out.println("추후 추가");
            throw new UsernameNotFoundException("비밀번호");
        }




//        if(userEntity.isEmpty()) {
//            throw new InternalAuthenticationServiceException("아이디 혹은 비밀번호가 틀렸습니다.");
//        }
//        return new PrincipalDetails(userEntity.get());


    }
}







