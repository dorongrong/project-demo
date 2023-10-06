package lee.projectdemo.item.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRegDto {

    @NotEmpty(message = "상품명을 입력해주세요.")
    @Size(min = 1, max = 30, message = "아이디는 1자 이상 30자 이하로 입력해주세요.")
    private String itemName;

    @NotEmpty(message = "상품 상세내용을 입력해주세요.")
    private String description;

    @NotEmpty(message = "상품 가격을 입력해주세요.")
    private int price; //int가 뭔가 있지 않나?

    private List<Image> images = new ArrayList<>();

}
