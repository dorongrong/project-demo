package lee.projectdemo.test;

import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.Address;
import lee.projectdemo.login.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    UserRepository userRepository;

/*
    @Autowired
    PlatformTransactionManager transactionManager;
    TransactionStatus status;

    @BeforeEach
    void beforeEach() {
        //트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }
*/

    @Test
    void save() {
        //given
        User user = new User("donix", "이성훈", "1234", new Address("123", "456", "103동"));

        //when
        User savedUser = userRepository.save(user);

        //then
        User findUser = userRepository.findById(user.getId()).get();
        assertThat(findUser).isEqualTo(savedUser);
    }

//    @Test
//    void updateItem() {
//        //given
//        Item item = new Item("item1", 10000, 10);
//        Item savedItem = itemRepository.save(item);
//        Long itemId = savedItem.getId();
//
//        //when
//        ItemUpdateDto updateParam = new ItemUpdateDto("item2", 20000, 30);
//        itemRepository.update(itemId, updateParam);
//
//        //then
//        Item findItem = itemRepository.findById(itemId).get();
//        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
//        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
//        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
//    }

//    @Test
//    void findItems() {
//        //given
//        Item item1 = new Item("itemA-1", 10000, 10);
//        Item item2 = new Item("itemA-2", 20000, 20);
//        Item item3 = new Item("itemB-1", 30000, 30);
//
//        log.info("repository={}", itemRepository.getClass());
//        itemRepository.save(item1);
//        itemRepository.save(item2);
//        itemRepository.save(item3);
//
//        //둘 다 없음 검증
//        test(null, null, item1, item2, item3);
//        test("", null, item1, item2, item3);
//
//        //itemName 검증
//        test("itemA", null, item1, item2);
//        test("temA", null, item1, item2);
//        test("itemB", null, item3);
//
//        //maxPrice 검증
//        test(null, 10000, item1);
//
//        //둘 다 있음 검증
//        test("itemA", 10000, item1);
//    }

//    void test(String itemName, Integer maxPrice, Item... items) {
//        List<Item> result = itemRepository.findAll(new ItemSearchCond(itemName, maxPrice));
//        assertThat(result).containsExactly(items);
//    }

}
