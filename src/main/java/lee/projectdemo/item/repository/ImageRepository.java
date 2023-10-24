package lee.projectdemo.item.repository;

import lee.projectdemo.item.item.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
