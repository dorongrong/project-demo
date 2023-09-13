package lee.projectdemo.login.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class UserRepository {

    private static Map<Long, User> userStore = new ConcurrentHashMap<>(); //static 사용
    private static long sequence = 0L;//static 사용

    public User save(User user) {
        user.setId(++sequence);
        userStore.put(user.getId(), user);
        log.info("save: user={}", user);
        return user;
    }

    public User findById(Long id) {
        return userStore.get(id);
    }

    public Optional<User> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    public void clearStore() {
        userStore.clear();
    }

}
