package lee.projectdemo.item.item;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String itemName;

    @NotEmpty
    private String description;

    @NotEmpty
    private String price;

    @NotEmpty
    private Long sellerId;

    public Item(String itemName, String description, String price, Long sellerId) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.sellerId = sellerId;
    }

    public Item(){

    }

}
