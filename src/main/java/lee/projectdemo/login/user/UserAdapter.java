package lee.projectdemo.login.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserAdapter extends User {

    private final User user;

}
