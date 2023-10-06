package lee.projectdemo.item.item;

import jakarta.persistence.*;
import lee.projectdemo.login.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    private String description;

    private String price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL) //아이템이랑 이미지의 연관관계 생각
    private List<Image> images = new ArrayList<>();

    public Item(Long id, String itemName, String description, String price, User user, List<Image> images) {
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.user = user;
        this.images = images;
    }

    public Item(){

    }

}
