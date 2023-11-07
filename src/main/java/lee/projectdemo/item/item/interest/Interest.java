package lee.projectdemo.item.item.interest;

import jakarta.persistence.*;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.login.user.User;
import lombok.Data;

@Entity
@Data
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public Interest(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    public Interest(){
    }

    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
