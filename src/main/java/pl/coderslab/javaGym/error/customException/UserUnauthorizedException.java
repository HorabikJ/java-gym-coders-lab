package pl.coderslab.javaGym.error.customException;

public class UserUnauthorizedException extends RuntimeException {

    private String message = "*User unauthorized exception.";

    public UserUnauthorizedException() {}

    public UserUnauthorizedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
