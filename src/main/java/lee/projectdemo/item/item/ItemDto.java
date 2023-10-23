package lee.projectdemo.item.item;


import lee.projectdemo.login.user.User;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {

    private Long itemId;
    private String itemName;
    private String description;
    private Integer price;
    private User user;
    private List<Image> images;

    public ItemDto(Long itemId, String itemName, String description, Integer price, User user, List<Image> images) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.user = user;
        this.images = images;
    }

    public ItemDto(){
    }


}
