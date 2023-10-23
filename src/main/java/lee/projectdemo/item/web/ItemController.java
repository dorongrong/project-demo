package lee.projectdemo.item.web;

import lee.projectdemo.item.item.ItemRegDto;
import lee.projectdemo.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //아이템 목록 조회
    @GetMapping
    public String items(Model moodel) {
        return "items/items";
    }

    //아이템 조회 get 수정 삭제 채팅 구현
    // /{itemsId}
    @GetMapping("/{itemId}")
    public String itemDetails() {
        return "items/details";
    }

    //아이템 추가 페이지 get
    // /add
    @GetMapping("/add")
    public String itemAddPage (@ModelAttribute("item") ItemRegDto itemRegDto){
        return "items/addItem";
    }

    //아이템 추가 post
    // /add

    @PostMapping("/add")
    public String itemAdd (@Validated @ModelAttribute("item") ItemRegDto itemRegDto, BindingResult bindingResult){
        
        // 아이템 상세 페이지로 이동
        return "items/add";
    }

    //아이템 수정 get
    // /{itemId}/edit

    //아이템 수정 post
    // /{itemId}/edit

    //삭제는 추후에

}
