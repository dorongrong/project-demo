package lee.projectdemo.chat.service;

import lee.projectdemo.item.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

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
                            createDynamicQueueAndBinding("chat.queue", "chat.exchange"
                                    , queueNameString, userLoginId);
                            //생성된 큐 구독
                            rabbitTemplate.receiveAndConvert("chat.queue." + itemId + "." + userLoginId);

                        } else {
                            //생성된 큐랑 바인딩 큐에 맞게 바꿔야함
                            rabbitTemplate.receiveAndConvert("yourQueueName." + itemId);
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

//    동적 queue 생성
    public void createDynamicQueueAndBinding(String queueName, String exchangeName, String routingKey, String userLoginId) {
        // 동적으로 큐 생성
        Queue dynamicQueue = new Queue(queueName + "." + routingKey + "." +userLoginId
                , true, false, false);
        TopicExchange dynamicExchange = new TopicExchange(exchangeName);

        // 큐를 RabbitMQ 브로커에 선언
        rabbitAdmin.declareQueue(dynamicQueue);

        // 생성된 큐의 이름 가져오기
        String dynamicQueueName = dynamicQueue.getName();

        // 동적으로 생성한 큐와의 바인딩 설정
        // new Queue(dynamicQueueName) 을 사용한건 rabbitmq에 있는 큐를 가져오기 위함
        Binding binding = BindingBuilder.bind(new Queue(dynamicQueueName))
                .to(dynamicExchange)
                .with(routingKey +".*");

        // RabbitMQ 브로커에 바인딩 설정
        rabbitAdmin.declareBinding(binding);

        System.out.println("Dynamic Queue '" + dynamicQueueName + "' created and binding set up.");
    }

//    동적 큐 구독
    @RabbitListener(queues = "#{dynamicQueueName(queueName)}")
    public void handleMessage(String message) {
        // 동적으로 큐를 구독하는 리스너 메서드
        System.out.println("Received dynamic message: " + message);
    }

    public String getDynamicQueueName(String queueName) {
        // 동적으로 큐 이름 생성 또는 가져오기 로직
        return "dynamic.queue." + System.currentTimeMillis();
    }

}
