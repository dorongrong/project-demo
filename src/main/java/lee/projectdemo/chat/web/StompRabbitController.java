package lee.projectdemo.chat.web;


import lee.projectdemo.chat.domain.ChatDto;
import lee.projectdemo.chat.domain.ChatUserState;
import lee.projectdemo.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
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

    //유저 상태확인
    private final TopicExchange userStatusExchange;

    private final RabbitTemplate template;

    private final ChatService chatService;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    @MessageMapping("chat.enter.{chatRoomId}.{buyerId}")
//    @SendTo("/exchange/chat.exchange/{chatRoomId}.{buyerId}")
    public void enter(@DestinationVariable String chatRoomId, @Payload ChatDto content, @DestinationVariable String buyerId){
        //메시지 고유 키값 설정

        String sendUserId = content.getSendUserId();

        content.setRegDate(LocalDateTime.now());
        content.setChatUserState(ChatUserState.ONLINE);

        System.out.println(sendUserId + "님이 입장하셨습니다.");
        //본인이 subscribe하고있는 큐랑 같은 라우팅키를 가진 큐로 보내도 신기하게 본인의 큐에는 메시지가 오지않음
        //나중에 이유를 찾아보자 (근데 왜 옜날엔 본인의 입장메시지도 잘 나왔지??)
        template.convertAndSend(CHAT_EXCHANGE_NAME, chatRoomId + "." + buyerId, content);

        //이건 여러 사용자가 보낸 모든 큐가 읽힌다는걸 유의
        listenerContainer.addQueueNames("chat.queue." + chatRoomId);

//        return content;
    }

    @MessageMapping("chat.exit.{chatRoomId}.{buyerId}")
    public void exit(@DestinationVariable String chatRoomId, @Payload ChatDto content, @DestinationVariable String buyerId){

        String sendUserId = content.getSendUserId();

        content.setRegDate(LocalDateTime.now());
        content.setChatUserState(ChatUserState.OFFLINE);

        System.out.println(sendUserId + "님이 나가셨습니다.");
        //본인이 subscribe하고있는 큐랑 같은 라우팅키를 가진 큐로 보내도 신기하게 본인의 큐에는 메시지가 오지않음
        //나중에 이유를 찾아보자 (근데 왜 옜날엔 본인의 입장메시지도 잘 나왔지??)
        template.convertAndSend(CHAT_EXCHANGE_NAME, chatRoomId + "." + buyerId, content);

    }

    //송신자가 보낸 메시지를 후처리후 퍼블리싱함
    @MessageMapping("chat.message.{chatRoomId}.{buyerId}")
    public void send(@DestinationVariable String chatRoomId, @Payload ChatDto content, @DestinationVariable String buyerId){

        //json으로 받은 메시지를 파싱
        String scontent = content.getMessage();
        String sendUserId = content.getSendUserId();
        System.out.println(scontent);

        content.setRegDate(LocalDateTime.now());
        content.setChatUserState(ChatUserState.CHAT);

        template.convertAndSend(CHAT_EXCHANGE_NAME, chatRoomId + "." + buyerId, content);

        System.out.println("content = " + content);
        //채팅 저장
        chatService.messageSave(content, buyerId);

    }

    @MessageMapping("chat.messageRead.{chatRoomId}")
    public ChatDto messageRead(@Payload ChatDto message){

        // Add logic to handle message read acknowledgment

        return message;
    }

}
