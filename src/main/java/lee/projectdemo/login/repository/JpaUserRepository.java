package lee.projectdemo.login.repository;


import jakarta.transaction.Transactional;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional //이건 나중에 빼야함
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final SpringDataJpaUserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override 
    //한번 더 보자
    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginIdLike(loginId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


}
