package lee.projectdemo.exception;

public class NoValue extends RuntimeException{

    public NoValue() {
    }

    public NoValue(String message) {
        super(message);
    }

}
