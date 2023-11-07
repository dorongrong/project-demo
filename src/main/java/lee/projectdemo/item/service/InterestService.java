package lee.projectdemo.item.service;


import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.interest.Interest;
import lee.projectdemo.item.repository.InterestRepository;
import lee.projectdemo.item.repository.ItemRepository;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;

    private final ItemRepository itemRepository;

    public void checkInterest(User user, Long itemId){

        if(interestRepository.findByUserIdAndItemId(user.getId(), itemId).isPresent()){
            // itemId와 userId를 둘다 가진 interest 테이블이 db에 존재할 경우 해당 아이템의 interestCount 감소 그리고 해당 테이블 삭제
            Item interestItem = itemRepository.findById(itemId).get();
            interestItem.setInterestCount(interestItem.getInterestCount() - 1);

            interestRepository.deleteByUserIdAndItemId(user.getId(), itemId);
        }
        else{
            //존재하지 않을경우 해당 아이템의 interestCount 증가 그리고 해당 테이블 생성
            //관심목록의 경우 해당 테이블 목록에서 User 의 Id를 조인해서 가져오면 됨

            Item interestItem = itemRepository.findById(itemId).get();
            interestItem.setInterestCount(interestItem.getInterestCount() + 1);

            Item item = itemRepository.findById(itemId).get();

            Interest interest = new Interest(user, item);
            interestRepository.save(interest);
        }
    }

}
