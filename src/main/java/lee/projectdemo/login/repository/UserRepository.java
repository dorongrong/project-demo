package lee.projectdemo.login.repository;

import lee.projectdemo.login.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    User findById(Long id);

    Optional<User> findByLoginId(String loginId);

    List<User> findAll();


}
