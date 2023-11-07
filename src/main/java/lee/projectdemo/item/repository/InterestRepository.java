package lee.projectdemo.item.repository;

import jakarta.transaction.Transactional;
import lee.projectdemo.item.item.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByUserIdAndItemId(Long userId, Long itemId);

    void deleteByUserIdAndItemId(Long userId, Long itemId);
}