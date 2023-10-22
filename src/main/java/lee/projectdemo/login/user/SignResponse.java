package lee.projectdemo.login.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;

    private String loginId;

    private String loginName;
 
    private UserRole role;

    private String token;

    public SignResponse(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.loginName = user.getLoginName();
        this.role = user.getUserRole();
    }

}
