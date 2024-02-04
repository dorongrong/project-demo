package lee.projectdemo.item.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lee.projectdemo.auth.PrincipalDetails;
import lee.projectdemo.item.aws.AwsS3Service;
import lee.projectdemo.item.imageService.FileStore;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.ItemDto;
import lee.projectdemo.item.item.ItemRegDto;
import lee.projectdemo.item.item.ItemSearchCond;
import lee.projectdemo.item.item.image.Image;
import lee.projectdemo.item.service.InterestService;
import lee.projectdemo.item.service.ItemService;
import lee.projectdemo.login.service.LoginService;
import lee.projectdemo.login.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lee.projectdemo.item.item.image.ImageStorageFolderName.PRODUCT_IMAGE_PATH;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final FileStore fileStore;
    private final LoginService loginService;
    private final InterestService interestService;

    private final AwsS3Service s3Service;

    //아이템 목록 조회
//    @PageableDefault(page = 0, size = 9, sort = "id", direction = Sort.Direction.DESC)
//    Pageable pageable,
    @GetMapping("/items")
    public String items(@ModelAttribute("itemSearch") ItemSearchCond cond, Model model,
                        @PageableDefault(page = 0, size = 9, sort = "item_id", direction = Sort.Direction.DESC) Pageable pageable) {

        //이미지가 들어가있는 itemList 반환
        Page<ItemDto> itemList = s3Service.addImageItemDto(itemService.findAllItemPage(cond, pageable), pageable);

        model.addAttribute("itemList", itemList);

        return "items/items";
    }

    //아이템 조회 get 수정 삭제 채팅 구현
    // /{itemsId}
    @GetMapping("/items/{itemId}")
    public String itemDetails(@PathVariable Long itemId, Model model, HttpServletRequest request) {
        Item item = itemService.getItem(itemId).get();

        String cToken = loginService.getCookie(request);
        Authentication user = loginService.getUserDetail(cToken);
        PrincipalDetails userDetails = (PrincipalDetails) user.getPrincipal();
        //토큰에서 빼온 유저 정보
        User tokenUser = userDetails.getUser();

        //사이트 방문자랑 판매자 동일 인물 비교 후 채팅버튼 변경
        if (tokenUser == item.getUser()) {
            model.addAttribute("chatButton", "내 채팅방");
        } else {
            model.addAttribute("buyerId", tokenUser.getId());
        }

        List<String> imageURLs = new ArrayList<>();

        //아이템 상세 이미지
        if (item.getImages().size() == 0) {
            //이미지가 없을 경우 대체이미지 삽입
            imageURLs.add(s3Service.loadEmptyImage());
        } else {
            for (Image image : item.getImages()) {
                imageURLs.add(s3Service.loadImage(image.getStoreFileName()));
            }
        }

        ItemDto itemDetails = new ItemDto(item.getId(), item.getItemName(), item.getDescription(), item.getPrice(),
                item.getBargain(), item.getCreatedAt(), item.getImages(), item.getState(), item.getInterestCount());

//        아이템 시간 계산 모델에 따로 넣는 이유는 locadatetime 이라는 데이터타입때문임
        String time = formatTime(itemDetails.getDate());

        model.addAttribute("time", time);
        model.addAttribute("item", itemDetails);
        model.addAttribute("images", imageURLs);
        model.addAttribute("user", tokenUser.getId());

        return "items/details";
    }

    //아이템 추가 페이지 get
    // /add
    @GetMapping("/items/add")
    public String itemAddPage(@ModelAttribute("item") ItemRegDto itemRegDto) {
        return "items/addItem";
    }

    //아이템 추가 post
    // /add

    @PostMapping("/items/add")
    public String itemAdd(@Valid @ModelAttribute("item") ItemRegDto itemRegDto, BindingResult bindingResult,
                          HttpServletRequest request, RedirectAttributes
                                  redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "items/addItem";
        }

        //아이템 저장 서비스에 값 넘겨줘서 저장 엔티티 주인은 걱정하지마라 어차피 둘다 저장할꺼다
        String cToken = loginService.getCookie(request);
        Authentication user = loginService.getUserDetail(cToken);
        PrincipalDetails userDetails = (PrincipalDetails) user.getPrincipal();
        //토큰에서 빼온 유저 정보
        User tokenUser = userDetails.getUser();

        //IOException 처리해라 filestore 처리하면 됨
//        List<Image> storeImageFiles = fileStore.storeFiles(itemRegDto.getImages());

        //S3 업로드와 동시에 List<Image>객체 반환(Image 객체에는 Item이 들어가있지 않은)
        //이미지를 넣지 않았을때
        if (itemRegDto.getImages().get(0).getOriginalFilename() == "") {
            Item item = new Item(itemRegDto.getItemName(), itemRegDto.getDescription(), itemRegDto.getPrice(),
                    itemRegDto.getBargain(), tokenUser, itemRegDto.getState(), 0);
            itemService.itemSave(item);

            redirectAttributes.addAttribute("itemId", item.getId());

            // 아이템 상세 페이지로 이동
            return "redirect:/items/{itemId}";
        } else {
            List<Image> storeImageFiles = s3Service.uploadFile(itemRegDto.getImages(), PRODUCT_IMAGE_PATH);

            //storeImageFiles에는 ID,Item 이 세팅 안돼있음
            Item item = new Item(itemRegDto.getItemName(), itemRegDto.getDescription(), itemRegDto.getPrice(),
                    itemRegDto.getBargain(), tokenUser, itemRegDto.getState(), 0);
            // 이미지 객체에 아이템 객체 넣어주기
            item.changeImages(storeImageFiles);

            itemService.itemSave(item);

            redirectAttributes.addAttribute("itemId", item.getId());

            // 아이템 상세 페이지로 이동
            return "redirect:/items/{itemId}";
        }
    }


    @PostMapping("/items/{itemId}/interest")
    public String interestItem(@PathVariable Long itemId, HttpServletRequest request, RedirectAttributes
            redirectAttributes) {

        //아이템 저장 서비스에 값 넘겨줘서 저장 엔티티 주인은 걱정하지마라 어차피 둘다 저장할꺼다
        String cToken = loginService.getCookie(request);
        Authentication user = loginService.getUserDetail(cToken);
        PrincipalDetails userDetails = (PrincipalDetails) user.getPrincipal();

        //토큰에서 빼온 유저 정보
        User tokenUser = userDetails.getUser();

        interestService.checkInterest(tokenUser, itemId);

        return "redirect:/items/{itemId}";
    }


    //아이템 수정 get
    // /{itemId}/edit

    //아이템 수정 post
    // /{itemId}/edit

    //삭제는 추후에

    //이미지
//    @GetMapping("/images/{storeFileName}")
//    public ResponseEntity loadImage(@PathVariable String storeFileName) {
//        //예외처리
//        System.out.println("들어감222");
//        String s3ObjectUrl = s3Service.loadImage(storeFileName);
//
//        return ResponseEntity<>(, HttpStatus.OK);;
//    }

    private String formatTime(LocalDateTime itemCreatedAt) {

        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(itemCreatedAt, now);
        if (duration.toDays() >= 1) {
            return duration.toDays() + "일";
        } else if (duration.toHours() >= 1) {
            return duration.toHours() + "시간";
        } else {
            return duration.toMinutes() + "분";
        }
    }

    

}
