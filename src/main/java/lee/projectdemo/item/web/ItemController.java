package lee.projectdemo.item.web;

import jakarta.servlet.http.HttpServletRequest;
import lee.projectdemo.item.imageService.FileStore;
import lee.projectdemo.item.item.ImageDto;
import lee.projectdemo.item.item.ItemRegDto;
import lee.projectdemo.item.service.ItemService;
import lee.projectdemo.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final FileStore fileStore;
    private final LoginService loginService;

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
    public String itemAdd (@Validated @ModelAttribute("item") ItemRegDto itemRegDto, BindingResult bindingResult,
                           HttpServletRequest request) throws IOException {
        //IOException 처리해라 filestore 처리하면 됨
        List<ImageDto> storeImageFiles = fileStore.storeFiles(itemRegDto.getImages());
        //아이템 저장 서비스에 값 넘겨줘서 저장 엔티티 주인은 걱정하지마라 어차피 둘다 저장할꺼다
        String cToken = loginService.getCookie(request);

        Authentication user = loginService.getUserDetail(cToken);


        // 아이템 상세 페이지로 이동
        return "items/add";
    }

    //아이템 수정 get
    // /{itemId}/edit

    //아이템 수정 post
    // /{itemId}/edit

    //삭제는 추후에

}
