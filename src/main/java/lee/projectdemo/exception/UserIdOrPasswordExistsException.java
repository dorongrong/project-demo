package lee.projectdemo.exception;

import org.springframework.security.core.AuthenticationException;

public class UserIdOrPasswordExistsException extends AuthenticationException {

    public UserIdOrPasswordExistsException(String message) {
        super(message);
    }

}
