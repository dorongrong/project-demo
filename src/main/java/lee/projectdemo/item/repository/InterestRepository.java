package lee.projectdemo.item.repository;

import lee.projectdemo.item.item.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByUserIdAndItemId(Long userId, Long itemId);

    List<Interest> findByUserId(Long userId);

    void deleteByUserIdAndItemId(Long userId, Long itemId);
}