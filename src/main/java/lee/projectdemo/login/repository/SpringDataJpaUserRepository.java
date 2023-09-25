package lee.projectdemo.login.repository;

import lee.projectdemo.login.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SpringDataJpaUserRepository extends JpaRepository<User, Long> {

//    List<User> findByLoginNameLike(String loginName);

    Optional<User> findByLoginIdLike(String loginId);

    
}
