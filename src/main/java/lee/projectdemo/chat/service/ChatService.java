package lee.projectdemo.chat.service;

import lee.projectdemo.chat.domain.ChatRoom;
import lee.projectdemo.chat.domain.ChatRoomDto;
import lee.projectdemo.chat.repository.SpringDataJpaChatRoomRepository;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.repository.ItemRepository;
import lee.projectdemo.login.repository.UserRepository;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final SpringDataJpaChatRoomRepository chatRoomRepository;

    // RabbitMQ 관리 API를 사용하여 큐 리스트를 가져오기

    public void subscribeQueue(List<Item> items, String userLoginId) {
        try {
            items.stream()
                    .map(Item::getId)
                    .forEach(itemId -> {
                                if (!isQueueExists(itemId)) {
                                    //큐가 존재하지 않을시 큐를 생성하고 구독
                                    System.out.println(itemId + "큐가 존재하지 않습니다");
                                    String queueNameString = Long.toString(itemId);
                                    createDynamicQueueAndBinding(queueNameString);
                                    //생성된 큐 구독

                                } else {
                                    //생성된 큐랑 바인딩 큐에 맞게 바꿔야함
                                    System.out.println(itemId + "큐가 존재합니다");
                                }
                            }
                    );
            //큐가 존재하지 않을시 null 이 반환된단다
//                        rabbitTemplate.receiveAndConvert("yourQueueName." + itemId);});}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isQueueExists(Long queueName) {

        String queueNameString = Long.toString(queueName);

        return rabbitAdmin.getQueueProperties(queueNameString) != null;
    }

    //    동적 queue 생성 여기서 routingKey는 itemid임
    public void createDynamicQueueAndBinding(String routingKey) {
        // 동적으로 큐 생성
        Queue dynamicQueue = new Queue("chat.queue." + routingKey
                , true, false, false);
        TopicExchange dynamicExchange = new TopicExchange("chat.exchange");

        // 큐를 RabbitMQ 브로커에 선언
        rabbitAdmin.declareQueue(dynamicQueue);

        // 생성된 큐의 이름 가져오기
        String dynamicQueueName = dynamicQueue.getName();

        // 동적으로 생성한 큐와의 바인딩 설정
        // new Queue(dynamicQueueName) 을 사용한건 rabbitmq에 있는 큐를 가져오기 위함
        Binding binding = BindingBuilder.bind(new Queue(dynamicQueueName))
                .to(dynamicExchange)
                .with(routingKey + ".#");

        // RabbitMQ 브로커에 바인딩 설정
        rabbitAdmin.declareBinding(binding);

        System.out.println("Dynamic Queue '" + dynamicQueueName + "' created and binding set up.");
    }

    public ChatRoomDto getOrSaveChatRoom(ChatRoomDto chatRoomDto) {

        //아이템 불러오기
        Item item = itemRepository.findById(chatRoomDto.getItemId()).get();

        //판매자
        User seller = userRepository.findByItem(item).get();

        //채팅방 요구자(buyer이 아님..)
        User requestUser = userRepository.findByLoginId(chatRoomDto.getSenderId()).get();

        String displayName;
        //판매자가 채팅방을 요구하는 경우
        if (seller == requestUser) {
            //채팅방이 존재하지 않으면 오류 터지게 해야함 판매자는 채팅방 못만듬
            if (!chatRoomRepository.existsByBuyerIdAndSellerAndItem(chatRoomDto.getBuyerId(), requestUser, item))
            {
                System.out.println("해당 사용자는 본인의 아이템에 속한 채팅방을 만들 수 없습니다.");
                //추후 수정 빈 채팅방이 아닌 다른걸 줘야함
                return new ChatRoomDto();
            }
            else{
                //아닐경우 채팅방 건내주자
                ChatRoom chatRoom = chatRoomRepository.findByItemAndSellerAndBuyerId(item, requestUser, chatRoomDto.getBuyerId());

                return new ChatRoomDto(item.getId(), String.valueOf(requestUser.getId()), chatRoom.getBuyer().getLoginId(),
                        chatRoom.getItem().getItemName(), chatRoom.getChats());
            }
        }
        else{
            //구매자가 채팅방을 요구한 경우 채팅방이 존재시 존재하는 채팅방을 주고 존재하지 않을시 채팅방 생성
            if (!chatRoomRepository.existsByBuyerAndSellerAndItem(requestUser, seller, item)) {
                //채팅방이 존재하지 않는 경우
                ChatRoom chatRoom = new ChatRoom(item, seller, requestUser);
                chatRoomRepository.save(chatRoom);

                return new ChatRoomDto(item.getId(), String.valueOf(requestUser.getId()), chatRoom.getSeller().getLoginId(),
                        chatRoom.getItem().getItemName(), chatRoom.getChats());
            }
            else {
                //채팅방이 존재하는 경우
                ChatRoom chatRoom = chatRoomRepository.findByItemAndSellerAndBuyer(item, seller, requestUser);
                return new ChatRoomDto(item.getId(), String.valueOf(requestUser.getId()), chatRoom.getSeller().getLoginId(),
                        chatRoom.getItem().getItemName(), chatRoom.getChats());
            }
        }
        
    }
}
