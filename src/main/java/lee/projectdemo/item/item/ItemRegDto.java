package lee.projectdemo.item.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRegDto {

    @NotEmpty(message = "상품명을 입력해주세요.")
    @Size(min = 1, max = 30, message = "상품명은 1자 이상 30자 이하로 입력해주세요.")
    private String itemName;

    @NotEmpty(message = "상품 상세내용을 입력해주세요.")
    private String description;

    //String 타입만 NotEmpty 가능
    @NotNull(message = "상품 가격을 입력해주세요.")
    @Positive(message = "유효한 숫자를 입력해주세요")
    private Integer price; //int가 뭔가 있지 않나?

    private List<MultipartFile> images = new ArrayList<>();

}
