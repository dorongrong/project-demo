package lee.projectdemo.chat.web;


import lee.projectdemo.chat.domain.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class StompRabbitController {

    private final RabbitTemplate template;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    @MessageMapping("/chat.enter.{chatRoomId}")
    public void enter(@Payload ChatDto content, @DestinationVariable String chatRoomId){
        //메시지 고유 키값 설정

        String sendUserId = content.getSendUserId();

        System.out.println("어서오세용" + chatRoomId);

        content.setMessage(sendUserId + "님이 입장하셨습니다.");
        content.setRegDate(LocalDateTime.now());

        template.convertAndSend(CHAT_EXCHANGE_NAME, chatRoomId, content); // exchange
        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("{chatRoomId}")
    public void enterTest(@Payload ChatDto content, @DestinationVariable String chatRoomId){
        //메시지 고유 키값 설정

        System.out.println("어서오세용" + chatRoomId);

        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("chat.enter.1.#")
    public void test2(@Payload ChatDto content){
        //메시지 고유 키값 설정

        System.out.println("어서오세용1");

        content.setMessage("님이 입장하셨습니다.");
        content.setRegDate(LocalDateTime.now());

        template.convertAndSend(CHAT_EXCHANGE_NAME, "1.#", content); // exchange
        template.convertAndSend(CHAT_EXCHANGE_NAME, "3.#", content); // exchange

        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("/chat.enter.1.#")
    public void test3(@Payload ChatDto content){
        //메시지 고유 키값 설정

        System.out.println("어서오세용2");

        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("/pub.chat.enter.1.#")
    public void test(@Payload ChatDto content){
        //메시지 고유 키값 설정

        System.out.println("어서오세용3");

        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("/pub/chat.enter.1.#")
    public void test1(@Payload ChatDto content){
        //메시지 고유 키값 설정

        System.out.println("어서오세용4");

        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }



    //송신자가 보낸 메시지를 후처리후 퍼블리싱함
    @MessageMapping("chat.message.{chatRoomId}")
    public void send(@DestinationVariable Long chatRoomId, @Payload ChatDto content){

        //json으로 받은 메시지를 파싱
        String scontent = content.getMessage();
        String sendUserId = content.getSendUserId();
        System.out.println(scontent);

        content.setRegDate(LocalDateTime.now());
        content.setChatRoomId(chatRoomId);

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId + "." + sendUserId, content);
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
}
