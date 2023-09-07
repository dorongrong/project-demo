package lee.projectdemo.login.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class User {

    private Long id;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String loginName;

    @NotEmpty
    private Number password;

    @NotEmpty
    private Address address;

}
