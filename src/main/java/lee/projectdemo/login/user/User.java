package lee.projectdemo.login.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "US")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column
    private String loginId;

    @NotEmpty
    private String loginName;

    @NotEmpty
    private String password;

    @Embedded
    private Address address;

    public User(String loginId, String loginName, String password, Address address){

    }

    public User(){

    }

}
