package lee.projectdemo.exception;

public class UserIdExistsException extends RuntimeException{

    public UserIdExistsException() {
    }

    public UserIdExistsException(String message) {
        super(message);
    }

}
