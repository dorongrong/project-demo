package lee.projectdemo.login.repository;


import jakarta.transaction.Transactional;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final SpringDataJpaUserRepository repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override 
    //한번 더 보자
    public Optional<User> findByLoginId(String loginId) {
        return repository.findByLoginIdLike(loginId);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }


}
