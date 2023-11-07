package lee.projectdemo.item.item;


import lee.projectdemo.item.item.image.Image;
import lee.projectdemo.login.user.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemDto {
    private Long itemId;
    private String itemName;
    private String description;
    private Integer price;
    private User user;

    private Integer interestCount;

    private Boolean bargain;
    private List<Image> images;
    //aws s3 용
    private List<String> imageUrls;
    private LocalDateTime date;

    private String state;


    public ItemDto(Long itemId, String itemName, String description, Integer price, Boolean bargain, User user,
                   List<Image> images, LocalDateTime date) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.bargain = bargain;
        this.user = user;
        this.images = images;
        this.date = date;
    }

    public ItemDto(Long itemId, String itemName, String description, Integer price, Boolean bargain, LocalDateTime date,
                   List<Image> images, String state, Integer interestCount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.date = date;
        this.bargain = bargain;
        this.images = images;
        this.state = state;
        this.interestCount = interestCount;
    }

    public ItemDto(){
    }


}
