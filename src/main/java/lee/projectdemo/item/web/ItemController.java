package lee.projectdemo.item.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.item.imageService.FileStore;
import lee.projectdemo.item.item.Image;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.ItemRegDto;
import lee.projectdemo.item.service.ItemService;
import lee.projectdemo.login.service.LoginService;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String itemAdd (@Valid @ModelAttribute("item") ItemRegDto itemRegDto, BindingResult bindingResult,
                           HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) {
            return "items/addItem";
        }

        //아이템 저장 서비스에 값 넘겨줘서 저장 엔티티 주인은 걱정하지마라 어차피 둘다 저장할꺼다
        String cToken = loginService.getCookie(request);
        Authentication user = loginService.getUserDetail(cToken);
        PrincipalDetails userDetails = (PrincipalDetails)user.getPrincipal();
        //토큰에서 빼온 유저 정보
        User tokenUser = userDetails.getUser();

        //IOException 처리해라 filestore 처리하면 됨
        List<Image> storeImageFiles = fileStore.storeFiles(itemRegDto.getImages());

        //storeImageFiles에는 ID,Item 이 세팅 안돼있음
        Item item = new Item(itemRegDto.getItemName(), itemRegDto.getDescription(), itemRegDto.getPrice(),
                tokenUser);
        // 이미지 객체에 아이템 객체 넣어주기
        item.changeImages(storeImageFiles);

        itemService.itemSave(item);

        // 아이템 상세 페이지로 이동
        return "items/addItem";
    }

    //아이템 수정 get
    // /{itemId}/edit

    //아이템 수정 post
    // /{itemId}/edit

    //삭제는 추후에

}
