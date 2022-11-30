package vn.com.example.streamservice.exception;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super("Email or password invalid");
    }
    
    public UnauthorizedException(String message) {
        super(message);
    }
}