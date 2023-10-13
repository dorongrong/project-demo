package lee.projectdemo.exception;

import org.springframework.security.core.AuthenticationException;

public class NoValue extends AuthenticationException{

    public NoValue(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoValue(String msg) {
        super(msg);
    }
}
