package lee.projectdemo.item.service;

import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.ItemDto;
import lee.projectdemo.item.item.ItemSearchCond;
import lee.projectdemo.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemDto> findAllItems(ItemSearchCond cond) {
        List<Item> itemList = itemRepository.findAll(cond);
        List<ItemDto> itemDtoList = Collections.synchronizedList(new ArrayList<>());

        for (Item item : itemList) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemId(item.getId());
            itemDto.setItemName(item.getItemName());
            itemDto.setDescription(item.getDescription());
            itemDto.setPrice(item.getPrice());
            itemDto.setUser(item.getUser());
            itemDto.setImages(item.getImages());

            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }

}
