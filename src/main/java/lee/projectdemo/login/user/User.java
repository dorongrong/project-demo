package lee.projectdemo.login.user;

import jakarta.persistence.*;
import lee.projectdemo.chat.domain.ChatRoom;
import lee.projectdemo.item.item.Item;
import lee.projectdemo.item.item.interest.Interest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "US")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique=true)
    private String loginId;

    private String loginName;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<Item> item = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<ChatRoom> sellingChatRooms  = new ArrayList<>();

    @OneToMany(mappedBy = "buyer")
    private List<ChatRoom> buyingChatRooms  = new ArrayList<>();

    @Embedded
    private Address address;

    public User(Long id, String loginId, String loginName, String password, List<Item> item, Address address) {
        this.id = id;
        this.loginId = loginId;
        this.loginName = loginName;
        this.password = password;
        this.item = item;
        this.address = address;
    }

    public User(String loginId, String loginName, String password, UserRole userRole, Address address) {
        this.loginId = loginId;
        this.loginName = loginName;
        this.password = password;
        this.userRole = userRole;
        this.address = address;
    }

    public User(){

    }

    //querydsl 사용시 무한루프 방지
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", userRole=" + userRole +
                ", address=" + address +
                '}';
    }
}
