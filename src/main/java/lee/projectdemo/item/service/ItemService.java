package lee.projectdemo.item.service;

import jakarta.transaction.Transactional;
import lee.projectdemo.chat.service.ChatService;
import lee.projectdemo.item.aws.AwsS3Service;
import lee.projectdemo.item.item.*;
import lee.projectdemo.item.item.interest.Interest;
import lee.projectdemo.item.repository.InterestRepository;
import lee.projectdemo.item.repository.ItemRepository;
import lee.projectdemo.login.repository.SpringDataJpaUserRepository;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ChatService chatService;
    private final ItemRepository itemRepository;
    private final InterestRepository interestRepository;
    private final AwsS3Service s3Service;

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

    public ItemRegDto getItemRegDto(Long itemId) {

        Item item = itemRepository.findById(itemId).get();
        ItemRegDto editItemDto = new ItemRegDto(item.getItemName(),item.getDescription(), item.getPrice(), item.getBargain(),
                item.getState(), s3Service.getImagesByItemId(itemId));

        return editItemDto;
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

    public Page<ItemDto> findUserItemPage(long userId, Pageable pageable) {

        List<Long> itemId = itemRepository.findUserIdsByUserId(userId);

        Page<ItemDto> itemList = itemRepository.findItemIdPage(itemId, pageable);

        return itemList;
    }

    public Page<ItemDto> findUserInterestItemPage(long userId, Pageable pageable) {

        List<Interest> interests = interestRepository.findByUserId(userId);

        List<Long> itemId = interests.stream()
                .map(interest -> interest.getItem().getId())
                .collect(Collectors.toList());

        Page<ItemDto> itemList = itemRepository.findItemIdPage(itemId, pageable);

        return itemList;
    }

    public void itemUpdate(Long userId, Long itemId, Item item){

        //사용자의 id가 판매자로있으면서 itemId가 같은 아이템을 업데이트함
        if (itemRepository.findById(itemId).get().getUser().getId().equals(userId)){
            itemRepository.update(itemId, item);
        }
    }



    //이건 아이템 아이디 줘야할듯
//    public Page<ItemDto> findInterestItemPage(long userId, Pageable pageable) {
//
//        Page<ItemDto> itemList = itemRepository.findAllPage(cond, pageable);
//
//        return itemList;
//    }

}
