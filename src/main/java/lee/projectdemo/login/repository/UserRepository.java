package lee.projectdemo.login.repository;

import lee.projectdemo.item.item.Item;
import lee.projectdemo.login.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByLoginId(String loginId);


//    Optional<User> findByLoginName(String loginName);

    List<User> findAll();


    Optional<User> findByItem(Item item);

}
