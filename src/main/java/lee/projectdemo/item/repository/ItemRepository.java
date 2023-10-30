package lee.projectdemo.item.repository;

import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.ItemDto;
import lee.projectdemo.item.item.ItemSearchCond;
import lee.projectdemo.item.item.ItemUpDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {


    Item save(Item item);

    void update(Long itemId, ItemUpDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond cond);

    //페이징용 아이템 찾기
    Page<ItemDto> findAllPage(ItemSearchCond cond, Pageable pageable);

}
