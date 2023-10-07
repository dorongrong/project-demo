package lee.projectdemo.item.config;

import jakarta.persistence.EntityManager;
import lee.projectdemo.item.repository.ItemRepository;
import lee.projectdemo.item.repository.ItemRepositoryImple;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {

    private final EntityManager em;

//    @Bean
//    public ItemService itemService() {
//        return new ItemServiceV1(itemRepository());
//    }

    @Bean
    public ItemRepository itemRepository() {
        return new ItemRepositoryImple(em);
    }

}
