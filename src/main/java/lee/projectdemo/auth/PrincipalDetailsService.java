package lee.projectdemo.auth;

import jakarta.transaction.Transactional;
import lee.projectdemo.exception.UserIdMismatchException;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
//시큐리티 설정에서 loginprocessingUrl("/login")
//요청이 오면 자동으로 UserDetailService 타입으로 IOC 되어 있는 loadUserByUsername 함수가 실행됨
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 생성된 security session 의 Auithentication 의 UserDetails에 들어감
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Optional<User> userEntity = userRepository.findByLoginId(loginId);

        //영속성이 종료된 후에 Item 조회를 위한 초기화
        if(userEntity.isPresent()){
            Hibernate.initialize(userEntity.get().getItem());
            return new PrincipalDetails(userEntity.get());
        }
        //사용자가 존재하지 않음
        else {
            throw new UsernameNotFoundException("아이디가 올바르지 않습니다.");
        }

    }
}







