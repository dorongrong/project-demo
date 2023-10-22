package lee.projectdemo.item.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    //아이템 목록 조회
    @GetMapping
    public String items() {
        return "items/items";
    }

    //아이템 조회 get
    // /{itemsId}

    //아이템 추가 페이지 get
    // /add

    //아이템 추가 post
    // /add

    //아이템 수정 get
    // /{itemId}/edit

    //아이템 수정 post
    // /{itemId}/edit

    //삭제는 추후에

}
