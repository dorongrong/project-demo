package lee.projectdemo.auth;

import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//시큐리티 설정에서 loginprocessingUrl("/login")
//요청이 오면 자동으로 UserDetailService 타입으로 IOC 되어 있는 loadUserByUsername 함수가 실행됨
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 생성된 security session 의 Auithentication 의 UserDetails에 들어감
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        // userEntity에 null 이 들어가는 상황이 가능한가? loadUserByUsername은 토큰 검증 이후 들어갈텐데...
        User userEntity = userRepository.findByLoginId(loginId).get();
        if(userEntity == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }
        return new PrincipalDetails(userEntity);


//        if(userEntity.isEmpty()) {
//            throw new InternalAuthenticationServiceException("아이디 혹은 비밀번호가 틀렸습니다.");
//        }
//        return new PrincipalDetails(userEntity.get());




    }
}







