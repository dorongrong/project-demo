package lee.projectdemo.chat.service;

import jakarta.transaction.Transactional;
import lee.projectdemo.chat.domain.ChatUserState;
import lee.projectdemo.chat.domain.ChatUserStateDto;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lee.projectdemo.chat.domain.ChatUserState.UNKNOWN;

@Service
@Transactional
public class UserStateService {

    private Map<Long, ChatUserState> userStatusMap = new ConcurrentHashMap<>();

    public ChatUserStateDto getUserState(Long userId) {
        ChatUserState chatUserState = userStatusMap.getOrDefault(userId, UNKNOWN);
        return new ChatUserStateDto(userId, chatUserState);
    }

    public void setUserState(Long userId, ChatUserState status) {
        userStatusMap.put(userId, status);
    }


}
