package lee.projectdemo.item.service;

import jakarta.transaction.Transactional;
import lee.projectdemo.chat.service.ChatService;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.ItemDto;
import lee.projectdemo.item.item.ItemFetchDto;
import lee.projectdemo.item.item.ItemSearchCond;
import lee.projectdemo.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ChatService chatService;
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
            itemDto.setBargain(item.getBargain());
            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }

    public Item itemSave(Item item){
        // 이 상태에선 이미지는 저장이 안되어있을꺼임 왜냐하면 아이템과 이미지의 연관관계에서 이미지가 연관관계 주인이다보니 이미지에 해줘야함
        // 아이템 큐 추가 -> 실패 해당 상황에선 아직 아이템이 저장이 안되서 밑의 item.getId가 안됨!! 어차피 매 페이지마다 큐를 생성하니
//        굳이 안만들어줘도됨
//        chatService.createDynamicQueueAndBinding(Long.toString(item.getId()));
        return itemRepository.save(item);
    }

    public Optional<Item> getItem(Long id){
        return itemRepository.findById(id);
    }

    //채팅방 상단에 아이템 설명을 위해 fetch api로 요청할때 사용
    public ItemFetchDto getFetchItem(Long id){

        Item item = itemRepository.findById(id).get();

        return new ItemFetchDto(item.getItemName(), item.getDescription(), item.getPrice(), item.getBargain());
    }

    //pageable 사용
    public Page<ItemDto> findAllItemPage(ItemSearchCond cond, Pageable pageable) {

        Page<ItemDto> itemList = itemRepository.findAllPage(cond, pageable);

        return itemList;
    }

}
