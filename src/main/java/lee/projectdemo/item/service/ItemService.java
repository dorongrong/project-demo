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
import java.util.Optional;

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

    public Item itemSave(Item item){
        // 이 상태에선 이미지는 저장이 안되어있을꺼임 왜냐하면 아이템과 이미지의 연관관계에서 이미지가 연관관계 주인이다보니 이미지에 해줘야함
        return itemRepository.save(item);
    }

    public Optional<Item> getItem(Long id){
        return itemRepository.findById(id);
    }

}
