package lee.projectdemo.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.ItemSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static lee.projectdemo.item.item.QItem.item;

@Repository
@RequiredArgsConstructor
@Transactional
public class ItemRepositoryImple implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ItemRepositoryImple(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

//    @Override
//    public void update(Long itemId, ItemUpdateDto updateParam) {
//        Item findItem = em.find(Item.class, itemId);
//        findItem.setItemName(updateParam.getItemName());
//        findItem.setPrice(updateParam.getPrice());
//        findItem.setQuantity(updateParam.getQuantity());
//    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return query
                .select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }
}
