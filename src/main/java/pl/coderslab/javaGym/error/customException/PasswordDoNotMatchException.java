package pl.coderslab.javaGym.error.customException;

public class PasswordDoNotMatchException extends RuntimeException {

    private String message = "*Old password do not match with actual one.";

    public PasswordDoNotMatchException() {}

    public PasswordDoNotMatchException(String message) {
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