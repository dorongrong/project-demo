package lee.projectdemo.item.repository;

import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.ItemSearchCond;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

//    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond cond);

}
