package lee.projectdemo.login.repository;


import lee.projectdemo.login.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class MemoryUserRepository implements UserRepository {

    private static Map<Long, User> userStore = new ConcurrentHashMap<>(); //static 사용
    private static long sequence = 0L;//static 사용

    @Override
    public User save(User user) {
        user.setId(++sequence);
        userStore.put(user.getId(), user);
        log.info("save: user={}", user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

//    @Override
//    public Optional<User> findByLoginId(String loginId) {
//        return findAll().stream()
//                .filter(m -> m.getLoginId().equals(loginId))
//                .findFirst();
//    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }


//    @Override
//     public Optional<User> findByLoginName(String loginName) {
//        return findAll().stream()
//                .filter(m -> m.getLoginName().equals(loginName))
//                .findFirst();
//    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    public void clearStore() {
        userStore.clear();
    }

}
