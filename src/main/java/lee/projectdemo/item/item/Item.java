package lee.projectdemo.item.item;

import jakarta.persistence.*;
import lee.projectdemo.login.user.User;
import lombok.Data;

import java.time.LocalDateTime;
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

    private Integer price;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL) //아이템이랑 이미지의 연관관계 생각
    private List<Image> images = new ArrayList<>();

    public Item(Long id, String itemName, String description, Integer price, User user, List<Image> images) {
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.user = user;
        this.images = images;
    }

    public Item(String itemName, String description, Integer price, User user) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.user = user;
    }

    public Item(){

    }

    //연관관계 편의 메소드
    //아이템에 이미지 리스트를 넣으면 이미지 하나하나에 아이템 객체를 넣는다.
    public void changeImages(List<Image> images) {
        this.images = images;
        for (Image image : images) {
            if (image != null) {
                image.changeItem(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", images=" + images +
                '}';
    }
}
