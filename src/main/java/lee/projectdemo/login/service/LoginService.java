package lee.projectdemo.login.service;

import lee.projectdemo.exception.UserIdExistsException;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.Address;
import lee.projectdemo.login.user.User;
import lee.projectdemo.login.user.UserDto;
import lee.projectdemo.login.user.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public User login(String loginId, String password) {
        return userRepository.findByLoginId(loginId).filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

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

    private boolean signUpIdExists(String loginId) {
        return userRepository.findByLoginId(loginId).isEmpty();
    }

}
