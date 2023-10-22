package lee.projectdemo.login.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lee.projectdemo.exception.UserIdExistsException;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.*;
import lee.projectdemo.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    //패스워드 인코더
    private final BCryptPasswordEncoder passwordEncoder;
    //
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

//    public User login(String loginId, String password) {
//        return userRepository.findByLoginId(loginId).filter(m -> m.getPassword().equals(password))
//                .orElse(null);
//    }

    public void signUp(UserDto userDto) {
//        if(signUpIdExists(userDto.getLoginId()) == false){
//            throw new UserIdExistsException("이미 존재하는 아이디입니다.");
//        }
        try {
            Address address = new Address(userDto.getAddressDto().getZipcode(),
                    userDto.getAddressDto().getStreetAdr(), userDto.getAddressDto().getDetailAdr());
            List<Item> item = new ArrayList<>();
            String rawPassword = userDto.getPassword();
            String encPassword = passwordEncoder.encode(rawPassword);
            User regisUser = new User(userDto.getLoginId(), userDto.getLoginName(),
                    encPassword, UserRole.USER , address);
            userRepository.save(regisUser);
        }
        catch (DataIntegrityViolationException e) {
            throw new UserIdExistsException("이미 존재하는 아이디입니다.");
        }
    }

    public SignResponse login(String loginId, String password) {
        try {
            User user = userRepository.findByLoginId(loginId).get();

            if(!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("비밀번호가 맞지 않습니다.");
            }

            return SignResponse.builder()
                    .id(user.getId())
                    .loginId(user.getLoginId())
                    .loginName(user.getLoginName())
                    .role(user.getUserRole())
                    .token(jwtProvider.createToken(user.getLoginId(), user.getUserRole()))
                    .build();

        } catch (NoSuchElementException e) {
            log.error("아이디가 맞지 않습니다.");
            throw new NoSuchElementException("아이디가 맞지 않습니다. ", e);
        } catch (BadCredentialsException e){
            log.error(e.getMessage());
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }



    private boolean signUpIdExists(String loginId) {
        return userRepository.findByLoginId(loginId).isEmpty();
    }

    public String getCookie(HttpServletRequest request){
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
