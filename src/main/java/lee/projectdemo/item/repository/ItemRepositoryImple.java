package lee.projectdemo.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.ItemDto;
import lee.projectdemo.item.item.ItemSearchCond;
import lee.projectdemo.item.item.ItemUpDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static lee.projectdemo.item.item.QItem.item;

@Repository
@Transactional //추후 변경
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

    @Override
    public void update(Long itemId, ItemUpDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setDescription(updateParam.getDescription());
        findItem.setPrice(updateParam.getPrice());
        findItem.setImages(updateParam.getImages());
    }

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

    //페이징용
    @Override
    public Page<ItemDto> findAllPage(ItemSearchCond cond, Pageable pageable){

        List<Item> items = getItemDtos(cond, pageable);
        
        //dto객체로 변형
        List<ItemDto> itemDtos = items.stream()
                .map(item -> new ItemDto(item.getId(), item.getItemName(), item.getDescription(), item.getPrice(),
                        item.getUser(), item.getImages(), item.getCreatedAt()))
                .collect(Collectors.toList());

        Long count = getCount(cond);

        return new PageImpl<>(itemDtos, pageable, count);

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

    //검색한 아이템의 총 수
    private Long getCount(ItemSearchCond cond) {

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        Long count = query
                .select(item.count())
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetchOne();
        return count;
    }

    private List<Item> getItemDtos(ItemSearchCond cond, Pageable pageable) {

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        List<Item> content = query
                .select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .offset(pageable.getOffset())   // 페이지 번호
                .limit(pageable.getPageSize())  // 페이지 사이즈
                .fetch();
        return content;
    }

}
