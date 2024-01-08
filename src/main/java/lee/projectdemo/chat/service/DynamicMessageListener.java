package lee.projectdemo.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DynamicMessageListener implements MessageListener {

    private final ChatService chatService;

    //여기서 메시지 저장(메시지는 모든 메시지가 저장됨)
    @Override
    public void onMessage(Message message) {
            System.out.println(message);
            // 특정 조건을 만족하는 메시지만 처리
            System.out.println("받은 메시지: " + new String(message.getBody()));
//        if (message.getBody() != null && new String(message.getBody()).contains("입장하셨습니다")) {
//            System.out.println(message);
//            // 특정 조건을 만족하는 메시지만 처리
//            System.out.println("받은 메시지: " + new String(message.getBody()));
//        }
    }
}
