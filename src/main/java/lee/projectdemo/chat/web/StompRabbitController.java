package lee.projectdemo.chat.web;


import lee.projectdemo.chat.domain.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class StompRabbitController {

    private final SimpleMessageListenerContainer listenerContainer;

    private final RabbitTemplate template;

    //test 12/23

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    @MessageMapping("chat.enter.{chatRoomId}.{userId}")
    public void enter(@DestinationVariable String chatRoomId, @Payload ChatDto content, @DestinationVariable String userId){
        //메시지 고유 키값 설정

        String sendUserId = content.getSendUserId();

        content.setMessage(sendUserId + "님이 입장하셨습니다.");
        content.setRegDate(LocalDateTime.now());

//        listenerContainer.addQueueNames("chat.queue." + chatRoomId);
        System.out.println(sendUserId + "님이 입장하셨습니다.");

        template.convertAndSend(CHAT_EXCHANGE_NAME, chatRoomId + "." + userId, content); // exchange

    }

    //송신자가 보낸 메시지를 후처리후 퍼블리싱함
    @MessageMapping("chat.message.{chatRoomId}.{userId}")
    public void send(@DestinationVariable String chatRoomId, @Payload ChatDto content, @DestinationVariable String userId){

        //json으로 받은 메시지를 파싱
        String scontent = content.getMessage();
        String sendUserId = content.getSendUserId();
        System.out.println(scontent);

        content.setRegDate(LocalDateTime.now());
//        content.setId(chatRoomId);

        template.convertAndSend(CHAT_EXCHANGE_NAME, chatRoomId + "." + userId, content);
        //template.convertAndSend( "room." + chatRoomId, chat);
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat);
    }

    @MessageMapping("chat.messageRead.{chatRoomId}")
    public ChatDto messageRead(@Payload ChatDto message){

        // Add logic to handle message read acknowledgment

        return message;
    }

    //receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatDto chat){

        System.out.println("received : " + chat.getMessage());
    }

//    @RabbitListener(queues = "chat.queue.16")
//    public void receive1(ChatDto chat){
//
//        System.out.println("received : 이건 메시지 리스너" + chat.getMessage());
//    }


}
