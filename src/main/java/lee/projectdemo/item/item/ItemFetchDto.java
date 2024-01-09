package lee.projectdemo.item.item;


import lombok.Data;

@Data
public class ItemFetchDto {

    private String itemName;
    private String description;
    private Integer price;

    private Boolean bargain;

    private String state;

    public ItemFetchDto(String itemName, String description, Integer price, Boolean bargain) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.bargain = bargain;
    }
}
