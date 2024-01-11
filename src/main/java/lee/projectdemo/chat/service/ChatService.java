package lee.projectdemo.chat.service;


import jakarta.transaction.Transactional;
import lee.projectdemo.chat.domain.Chat;
import lee.projectdemo.chat.domain.ChatDto;
import lee.projectdemo.chat.domain.ChatRoom;
import lee.projectdemo.chat.repository.SpringDataJpaChatRepository;
import lee.projectdemo.chat.repository.SpringDataJpaChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final SpringDataJpaChatRepository chatRepository;

    private final SpringDataJpaChatRoomRepository chatRoomRepository;

    public void messageSave(ChatDto chatDto, String buyerId) {

        //.get()이 없을때도 처리해야함 <-- 01/03 이거 다시해야함 chatRoomId를 못찾음
        ChatRoom chatRoom = chatRoomRepository.findByItemIdAndBuyerId(chatDto.getChatRoomId(), Long.parseLong(buyerId));

        //사용자 Id
        Long requestUserId = Long.parseLong(chatDto.getSendUserId());

        Chat chat = new Chat(requestUserId, chatDto.getReadCount(),
                chatDto.getMessage(),chatRoom);

        chatRepository.save(chat);
    }

}
