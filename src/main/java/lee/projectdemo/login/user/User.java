package lee.projectdemo.login.user;

import jakarta.persistence.*;
import lee.projectdemo.item.item.Item;
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

    public User(String loginId, String loginName, String password, Address address) {
        this.loginId = loginId;
        this.loginName = loginName;
        this.password = password;
        this.address = address;
    }

    public User(){

    }

}
