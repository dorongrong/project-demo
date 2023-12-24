package lee.projectdemo.chat.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class DynamicMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        if (message.getBody() != null && new String(message.getBody()).contains("입장하셨습니다")) {
            // 특정 조건을 만족하는 메시지만 처리
            System.out.println("받은 메시지: " + new String(message.getBody()));
        }
    }
}
