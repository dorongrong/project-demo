package lee.projectdemo;

import lee.projectdemo.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final UserRepository userRepository;

    /**
     * 테스트용 데이터 추가
     */
    //@PostConstruct
//    참고로 이 기능 대신 @PostConstruct
//    를 사용할 경우 AOP 같은 부분이 아직 다 처리되지 않은
//    시점에 호출될 수 있기 때문에, 간혹 문제가 발생할 수 있다. 예를 들어서 @Transactional 과 관련된
//    AOP가 적용되지 않은 상태로 호출될 수 있다.
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
    }

}